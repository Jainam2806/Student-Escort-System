package com.example.studentescortsystem.parent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.studentescortsystem.Profile.ForgotPasswordActivity;
import com.example.studentescortsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class ParentLogin extends AppCompatActivity {

    private EditText editTextLoginEmail, editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private static final String TAG ="ParentLogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);

        getSupportActionBar().setTitle("Login");

        editTextLoginEmail =findViewById(R.id.editText_parent_login_email);
        editTextLoginPwd=findViewById(R.id.editText_parent_login_password);
        progressBar=findViewById(R.id.progressBar);

        authProfile =FirebaseAuth.getInstance();

        //Forgit Button
        Button buttonForgotPassword =findViewById(R.id.button_parent_forgot_password);
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ParentLogin.this, "you can reset your password now!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ParentLogin.this, ForgotPasswordActivity.class));

            }
        });
        //show Hide Password using Eye Icon
        ImageView imageViewShowHidePwd =findViewById(R.id.imageview_parent_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //If Password is visible then hide it
                    editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //Change Icon
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
                }
                else{
                    editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);
                }
            }
        });

        //login User
        Button butttonLogin =findViewById(R.id.button_parent_login);
        butttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail =editTextLoginEmail.getText().toString();
                String textPwd =editTextLoginPwd.getText().toString();

                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(ParentLogin.this,"Please enter Your EmailId",Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("Email is Required");
                    editTextLoginEmail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(ParentLogin.this,"Please re-enter Your EmailId",Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("Valid Email is required");
                    editTextLoginEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(ParentLogin.this,"Please enter Your Password",Toast.LENGTH_LONG).show();
                    editTextLoginPwd.setError("Password is Required");
                    editTextLoginPwd.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail,textPwd);
                }
            }
        });
    }

    private void loginUser(String email, String pwd) {
        authProfile.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ParentLogin.this,"You are logged in now",Toast.LENGTH_LONG).show();


                    //Get instance of the current User
                    FirebaseUser firebaseUser=authProfile.getCurrentUser();
                    if(firebaseUser.isEmailVerified()){
                        Toast.makeText(ParentLogin.this, "You are logged in.", Toast.LENGTH_SHORT).show();


                        //Open User Profile
                        //Start the UserProfileActivity
                        startActivity(new Intent(ParentLogin.this,ParentUserProfile.class));
                        finish();

                    }
                    else{
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialog();
                    }
                }else{
                    try{
                        throw  task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        editTextLoginEmail.setError("User does not exists or is no longer valid.Please register again.");
                        editTextLoginEmail.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        editTextLoginEmail.setError("Invalid credentials.Kindly ,check and re-enter.");
                        editTextLoginEmail.requestFocus();
                    }
                    catch (Exception e) {
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(ParentLogin.this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        //Setup the Alert Builder
        AlertDialog.Builder builder=new AlertDialog.Builder(ParentLogin.this);
        builder.setTitle("Email is not verified");
        builder.setMessage("Please verify your email now.You can not login without email verification");

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

    //Check if user is already logged in.In such case, straightway take the the User to the User's profile
    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser() !=null){
            Toast.makeText(ParentLogin.this, "Already Logged In!", Toast.LENGTH_SHORT).show();

            //Start the UserProfileActivity
            startActivity(new Intent(ParentLogin.this,ParentUserProfile.class));
            finish();
        }
        else{
            Toast.makeText(ParentLogin.this, "You can LogIn Now!", Toast.LENGTH_SHORT).show();

        }
    }
}