package com.example.studentescortsystem.school;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.studentescortsystem.MainActivity;
import com.example.studentescortsystem.Profile.ChangePasswordActivity;
import com.example.studentescortsystem.Profile.DeleteProfileActivity;
import com.example.studentescortsystem.Profile.UpdateEmailActivity;
import com.example.studentescortsystem.Profile.UpdateProfileActivity;
import com.example.studentescortsystem.Profile.UploadProfilePicActivity;
import com.example.studentescortsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SchoolProfile extends AppCompatActivity {

    private TextView textViewWelcome,textViewFullName,textViewEmail,textviewPincode,textViewMobile;
    private ProgressBar progressBar;
    private String fullName ,email,pincode,mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_profile);

        getSupportActionBar().setTitle("Home");

        //Generate Button
        Button buttonRegister = findViewById(R.id.button_school_generate);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SchoolProfile.this, SchoolChooseWay.class);
                startActivities(new Intent[]{intent});
            }
        });

        //Binding Views
        textViewWelcome =findViewById(R.id.textview_show_welcome);
        textViewFullName=findViewById(R.id.textview_show_school_name);
        textViewEmail=findViewById(R.id.textview_show_school_email);
        textViewMobile=findViewById(R.id.textview_show_school_mobile);
        textviewPincode=findViewById(R.id.textview_show_school_pincode);
        progressBar =findViewById(R.id.progressBar);

        //Set OnClickListener on ImageView to open UploadProfilePicActivity
        imageView =findViewById(R.id.imageview_profile_dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SchoolProfile.this, UploadProfilePicActivity.class);
                startActivity(intent);
            }
        });

        authProfile =FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= authProfile.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(SchoolProfile.this, "Something went wrong!User's details are not available at the moment", Toast.LENGTH_SHORT).show();

        }else{
            checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
    }

    //User coming to UserProfileActivity after Successful registration
    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if(!firebaseUser.isEmailVerified()){
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        //Setup the Alert Builder
        AlertDialog.Builder builder=new AlertDialog.Builder(SchoolProfile.this);
        builder.setTitle("Email is not verified");
        builder.setMessage("Please verify your email now.You can not login without email verification next time");

        //open email app if user clicks/taps  continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//To email app in new window and not within our app
                startActivities(new Intent[]{intent});
            }
        });

        //create the AlertDialog
        AlertDialog alertDialog=builder.create();

        //show the alertDialog
        alertDialog.show();
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extracting User reference from Database for "Registered User"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered school");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteSchoolDetails readUserDetails =snapshot.getValue(ReadWriteSchoolDetails.class);
                if(readUserDetails !=null){
                    fullName =firebaseUser.getDisplayName();
                    email =firebaseUser.getEmail();
                    pincode=readUserDetails.pincode;
                    mobile =readUserDetails.mobile;

                    textViewWelcome.setText("Welcome, " +fullName+"!");
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewMobile.setText(mobile);
                    textviewPincode.setText(pincode);


                    //Set User Dp
                    Uri uri =firebaseUser.getPhotoUrl();

                    //ImageViewer setImagerURI() should not be used with regular URIs.So we are using Picasso
                    Picasso.get().load(uri).into(imageView);
                }else {
                    Toast.makeText(SchoolProfile.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(SchoolProfile.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    //Creating ActionBar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate  menu items
        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //When any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id =item.getItemId();

        if(id == R.id.menu_refresh){
            //Refresh Activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }

        else if(id == R.id.menu_update_profile){
            Intent intent =new Intent(SchoolProfile.this, UpdateProfileActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_update_profile) {
            Intent intent = new Intent(SchoolProfile.this, UpdateEmailActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.menu_settings) {
            Toast.makeText(SchoolProfile.this, "menu_settings", Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.menu_change_password) {
            Intent intent = new Intent(SchoolProfile.this, ChangePasswordActivity.class);
            startActivity(intent);
        }else if(id == R.id.menu_delete_profile) {
            Intent intent = new Intent(SchoolProfile.this, DeleteProfileActivity.class);
            startActivity(intent);
        }else if(id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(SchoolProfile.this, "Logged Out", Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(SchoolProfile.this, MainActivity.class);

            //clear stack to prevent user coming back to  user-activity by pressing back button after Logging out
            intent.setFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();//close Userprofile activity
        }
        else {
            Toast.makeText(SchoolProfile.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}