package com.example.studentescortsystem.school;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.studentescortsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchoolRegister extends AppCompatActivity {

    private EditText editTextRegisterFullName ,editTextRegisterPincode,editTextRegisterSchoolName,editTextRegisterEmail,editTextRegisterMobile,editTextRegisterPwd,editTextRegisterConfirmPwd;
    private ProgressBar progressBar;

    private  DatePickerDialog picker;
    private static final String TAG="SchoolRegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_register);

        getSupportActionBar().setTitle("SchoolRegister");
        Toast.makeText(SchoolRegister.this,"You can register now",Toast.LENGTH_LONG).show();

        progressBar =findViewById(R.id.progressBar);
        editTextRegisterEmail =findViewById(R.id.editText_school_register_email);
        editTextRegisterPincode=findViewById(R.id.editText_school_register_pincode);
        editTextRegisterMobile =findViewById(R.id.editText_school_register_mobile);
        editTextRegisterPwd=findViewById(R.id.editText_school_register_password);
        editTextRegisterConfirmPwd=findViewById(R.id.editText_school_register_confirm_password);
        editTextRegisterSchoolName =findViewById(R.id.editText_school_register_school_name);


        Button buttonParentRegister = findViewById(R.id.button_school_register);
        buttonParentRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Obtain the entered
                String textEmail=editTextRegisterEmail.getText().toString();
                String textMobile =editTextRegisterMobile.getText().toString();
                String textPwd =editTextRegisterPwd.getText().toString();
                String textConfirmPwd =editTextRegisterConfirmPwd.getText().toString();
                String textPincode = editTextRegisterPincode.getText().toString();
                String textSchoolName = editTextRegisterSchoolName.getText().toString();

                //Validate Mobile Number Matcher and pattern(regular Expression
                String mobileRegex="[6-9][0-9]{9}";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher =mobilePattern.matcher(textMobile);


                //database
                //Fire Database
                Map<String,String> v=new HashMap<>();
                v.put("School",textSchoolName);
                v.put("Email",textEmail);
                v.put("Mobile",textMobile);
                v.put("Roll No",textPincode);
                v.put("Password",textPwd);



                FirebaseFirestore.getInstance().collection("School").document("School Details").collection(textSchoolName)
                        .add(v).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
                if(TextUtils.isEmpty(textSchoolName)){
                    Toast.makeText(SchoolRegister.this,"Please enter your full name",Toast.LENGTH_LONG).show();
                    editTextRegisterSchoolName.setError("Full Name is Required");
                    editTextRegisterSchoolName.requestFocus();
                }
                else if(TextUtils.isEmpty(textSchoolName)){
                    Toast.makeText(SchoolRegister.this,"Please enter your School",Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("School  is required");
                    editTextRegisterEmail.requestFocus();
                }else if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(SchoolRegister.this,"Please enter your Email",Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(SchoolRegister.this,"Please re-enter your Email",Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Valid Email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(SchoolRegister.this,"Please enter your Mobile no.",Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile no. is Required");
                    editTextRegisterMobile.requestFocus();
                }
                else if (TextUtils.isEmpty(textPincode)) {
                    Toast.makeText(SchoolRegister.this,"Please enter your Pincode no.",Toast.LENGTH_LONG).show();
                    editTextRegisterPincode.setError("Pincode no. is Required");
                    editTextRegisterPincode.requestFocus();
                }
                else if (textMobile.length() != 10) {
                    Toast.makeText(SchoolRegister.this,"Please re-enter your Mobile no.",Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("valid Mobile no. is Required");
                    editTextRegisterMobile.requestFocus();
                }
                else if (textPincode.length() != 6) {
                    Toast.makeText(SchoolRegister.this,"Please re-enter your pincode no.",Toast.LENGTH_LONG).show();
                    editTextRegisterPincode.setError("valid Pincode no. is Required");
                    editTextRegisterPincode.requestFocus();
                }
                else if(!mobileMatcher.find()){
                    Toast.makeText(SchoolRegister.this,"Please re-enter your Mobile no.",Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("valid Mobile no. is Required");
                    editTextRegisterMobile.requestFocus();
                }
                else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(SchoolRegister.this,"Please enter Password",Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Password is Required");
                    editTextRegisterPwd.requestFocus();
                }
                else if (textPwd.length() <6) {
                    Toast.makeText(SchoolRegister.this,"Please re-enter your Password",Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("minimum length is 6");
                    editTextRegisterPwd.requestFocus();
                }
                else if (TextUtils.isEmpty(textConfirmPwd)) {
                    Toast.makeText(SchoolRegister.this,"Please enter Password",Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("Password is Required");
                    editTextRegisterConfirmPwd.requestFocus();
                }
                else if (!textPwd.equals(textConfirmPwd)) {
                    Toast.makeText(SchoolRegister.this,"Please re-enter your ConfirmPassword",Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("Confirm password must same as Password ");
                    editTextRegisterConfirmPwd.requestFocus();

                    //cleared the entered Password
                    editTextRegisterConfirmPwd.clearComposingText();
                    editTextRegisterPwd.clearComposingText();
                }else{
                    registerUser(textSchoolName,textPincode,textEmail,textMobile,textPwd,textConfirmPwd);
                }

            }
        });

    }


    private void registerUser(String textSchoolName,String textPincode, String textEmail,  String textMobile,  String textPwd, String textConfirmPwd) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail,textPwd).addOnCompleteListener(SchoolRegister.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SchoolRegister.this,"User registered successfully",Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser =auth.getCurrentUser();

                            //Update Dispay Name of User
                            UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder().setDisplayName(textSchoolName).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                            //Enter user data into the Firebase Realtime databse.
                            ReadWriteSchoolDetails writeUserDetails =new ReadWriteSchoolDetails( textSchoolName,textPincode,textMobile);

                            //Extracting User reference from Database for "Register Users"
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered school");

                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        //send verification
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(SchoolRegister.this,"User registered successfully.Please verify your email",Toast.LENGTH_LONG).show();

                                        //Open user profile after successful registration
                                        Intent intent =new Intent(SchoolRegister.this,SchoolProfile.class);
                                        //To prevent user from returning back to register activity on pressing back button after registration
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivities(new Intent[]{intent});
                                        finish();//to close regestration
                                    }else{
                                        Toast.makeText(SchoolRegister.this,"User registered failed.Please verify your email",Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                            //send verification
                            firebaseUser.sendEmailVerification();

                            //Open user profile after successful registration
                            Intent intent =new Intent(SchoolRegister.this,SchoolProfile.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivities(new Intent[]{intent});
                            finish();

                        }else{
                            try{
                                throw task.getException();
                            }catch(FirebaseAuthWeakPasswordException e){
                                editTextRegisterPwd.setError("Your Password is too week.Kindly use a mix of alphabets,numbers and special character");
                                editTextRegisterPwd.requestFocus();
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                editTextRegisterPwd.setError("Your email is invalid or already in use.Kindly re-enter.");
                                editTextRegisterPwd.requestFocus();
                            }catch(FirebaseAuthUserCollisionException e){
                                editTextRegisterPwd.setError("User is already registered with this email.Use another email ");
                                editTextRegisterPwd.requestFocus();
                            }catch (Exception e){
                                Log.e(TAG,e.getMessage());
                                Toast.makeText(SchoolRegister.this,e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}