package com.example.studentescortsystem.parent;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParentRegister extends AppCompatActivity {

    private EditText editTextRegisterFullName ,editTextRegisterRollNo,editTextRegisterSchoolName,editTextRegisterEmail,editTextRegisterDoB,editTextRegisterMobile,editTextRegisterPwd,editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private  DatePickerDialog picker;
    private RadioButton radioButtonRegisterGenderSelected;
    private static final String TAG="ParentRegisterActivity";

    public String textFullName;
    public String textEmail;
    public String textDoB;
    public String textMobile;
    public String textPwd;
    public String textConfirmPwd;
    public String textRollNo;
    public String textSchoolName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_register);

        getSupportActionBar().setTitle("ParentRegister");
        Toast.makeText(ParentRegister.this,"You can register now",Toast.LENGTH_LONG).show();

        progressBar =findViewById(R.id.progressBar);
        editTextRegisterFullName =findViewById(R.id.editText_parent_register_full_name);
        editTextRegisterEmail =findViewById(R.id.editText_parent_register_email);
        editTextRegisterDoB =findViewById(R.id.editText_parent_register_dob);
        editTextRegisterMobile =findViewById(R.id.editText_parent_register_mobile);
        editTextRegisterPwd=findViewById(R.id.editText_parent_register_password);
        editTextRegisterConfirmPwd=findViewById(R.id.editText_parent_register_confirm_password);
        editTextRegisterRollNo =findViewById(R.id.editText_parent_register_Rollno);
        editTextRegisterSchoolName =findViewById(R.id.editText_parent_register_school_name);

        //Radio button
        radioGroupRegisterGender =findViewById(R.id.radio_group_parent_register_gender);
        radioGroupRegisterGender.clearCheck();

        //Setting up DatePicker on EditText
        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calender =Calendar.getInstance();
                int day =calender.get(Calendar.DAY_OF_MONTH);
                int month =calender.get(Calendar.MONTH);
                int year=calender.get(Calendar.YEAR);

                //Date Picker Dialog
                picker =new DatePickerDialog(ParentRegister.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        editTextRegisterDoB.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },year, month,day);
                picker.show();
            }
        });
        Button buttonParentRegister = findViewById(R.id.button_parent_register);
        buttonParentRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected=findViewById(selectedGenderId);


                //Obtain the entered
                 textFullName =editTextRegisterFullName.getText().toString();
                 textEmail=editTextRegisterEmail.getText().toString();
                 textDoB=editTextRegisterDoB.getText().toString();
                 textMobile =editTextRegisterMobile.getText().toString();
                 textPwd =editTextRegisterPwd.getText().toString();
                 textConfirmPwd =editTextRegisterConfirmPwd.getText().toString();
                 textRollNo =editTextRegisterRollNo.getText().toString();
                 textSchoolName = editTextRegisterSchoolName.getText().toString();
                String textGender;   //Can't obtain the value is not selected


                //Fire Database
                Map<String,String> v=new HashMap<>();
                v.put("Name",textFullName);
                v.put("Email",textEmail);
                v.put("DoB",textDoB);
                v.put("Mobile",textMobile);
                v.put("Password",textPwd);
                v.put("Roll No",textRollNo);
                v.put("School",textSchoolName);

                FirebaseFirestore.getInstance().collection("Parent").document("Student Details").collection(textFullName)
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


                //Validate Mobile Number Matcher and pattern(regular Expression
                String mobileRegex="[6-9][0-9]{9}";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher =mobilePattern.matcher(textMobile);

                if(TextUtils.isEmpty(textFullName)){
                    Toast.makeText(ParentRegister.this,"Please enter your full name",Toast.LENGTH_LONG).show();
                    editTextRegisterFullName.setError("Full Name is Required");
                    editTextRegisterFullName.requestFocus();
                }
                else if(TextUtils.isEmpty(textSchoolName)){
                    Toast.makeText(ParentRegister.this,"Please enter your School",Toast.LENGTH_LONG).show();
                    editTextRegisterSchoolName.setError("School  is required");
                    editTextRegisterSchoolName.requestFocus();
                }else if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(ParentRegister.this,"Please enter your Email",Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Email is required");
                    editTextRegisterEmail.requestFocus();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(ParentRegister.this,"Please re-enter your Email",Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Valid Email is required");
                    editTextRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textRollNo)) {
                    Toast.makeText(ParentRegister.this,"Please enter your Roll No.",Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("RollNo. is required");
                    editTextRegisterEmail.requestFocus();
                }else if (TextUtils.isEmpty(textDoB)){
                    Toast.makeText(ParentRegister.this,"Please enter your Date of Birth",Toast.LENGTH_LONG).show();
                    editTextRegisterDoB.setError("date of Birth is required");
                    editTextRegisterDoB.requestFocus();
                } else if (radioGroupRegisterGender.getCheckedRadioButtonId() ==-1) {
                    Toast.makeText(ParentRegister.this,"Please select Gender",Toast.LENGTH_LONG).show();
                    radioButtonRegisterGenderSelected.setError("Gender is required");
                    radioButtonRegisterGenderSelected.requestFocus();
                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(ParentRegister.this,"Please enter your Mobile no.",Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile no. is Required");
                    editTextRegisterMobile.requestFocus();
                }
                else if (textMobile.length() != 10) {
                    Toast.makeText(ParentRegister.this,"Please re-enter your Mobile no.",Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("valid Mobile no. is Required");
                    editTextRegisterMobile.requestFocus();
                }
                else if(!mobileMatcher.find()){
                    Toast.makeText(ParentRegister.this,"Please re-enter your Mobile no.",Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("valid Mobile no. is Required");
                    editTextRegisterMobile.requestFocus();
                }
                else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(ParentRegister.this,"Please enter Password",Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Password is Required");
                    editTextRegisterPwd.requestFocus();
                }
                else if (textPwd.length() <6) {
                    Toast.makeText(ParentRegister.this,"Please re-enter your Password",Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("minimum length is 6");
                    editTextRegisterPwd.requestFocus();
                }
                else if (TextUtils.isEmpty(textConfirmPwd)) {
                    Toast.makeText(ParentRegister.this,"Please enter Password",Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("Password is Required");
                    editTextRegisterConfirmPwd.requestFocus();
                }
                else if (!textPwd.equals(textConfirmPwd)) {
                    Toast.makeText(ParentRegister.this,"Please re-enter your ConfirmPassword",Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("Confirm password must same as Password ");
                    editTextRegisterConfirmPwd.requestFocus();

                    //cleared the entered Password
                    editTextRegisterConfirmPwd.clearComposingText();
                    editTextRegisterPwd.clearComposingText();
                }else{
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName,textSchoolName,textRollNo,textEmail,textDoB,textGender,textMobile,textPwd,textConfirmPwd);
                }

            }
        });

    }


    private void registerUser(String textFullName,String textSchoolName,String textRollNo, String textEmail, String textDoB,String textGender, String textMobile,  String textPwd, String textConfirmPwd) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail,textPwd).addOnCompleteListener(ParentRegister.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ParentRegister.this,"User registered successfully",Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser =auth.getCurrentUser();

                            //Update Dispay Name of User
                            UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                            firebaseUser.updateProfile(profileChangeRequest);

                            //Enter user data into the Firebase Realtime databse.
                            ReadWriteUserDetails writeUserDetails =new ReadWriteUserDetails( textSchoolName,textRollNo,textDoB,textGender,textMobile);

                            //Extracting User reference from Database for "Register Users"
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered users");

                            referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        //send verification
                                        firebaseUser.sendEmailVerification();
                                        Toast.makeText(ParentRegister.this,"User registered successfully.Please verify your email",Toast.LENGTH_LONG).show();

                                        //Open user profile after successful registration
                                        Intent intent =new Intent(ParentRegister.this, ParentUserProfile.class);
                                        //To prevent user from returning back to register activity on pressing back button after registration
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivities(new Intent[]{intent});
                                        finish();//to close regestration
                                    }else{
                                        Toast.makeText(ParentRegister.this,"User registered failed.Please verify your email",Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                            //send verification
                            firebaseUser.sendEmailVerification();

                            //Open user profile after successful registration
                            Intent intent =new Intent(ParentRegister.this, ParentUserProfile.class);
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
                                Toast.makeText(ParentRegister.this,e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}