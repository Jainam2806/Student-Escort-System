package com.example.studentescortsystem.school;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentescortsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;


public class School_Input_Password extends AppCompatActivity {


    private EditText editTextLoginEmail, editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private static final String TAG ="School_Input_Password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_input_password);

        getSupportActionBar().setTitle("Enter ID_Password");

        progressBar=findViewById(R.id.progressBar);

        authProfile =FirebaseAuth.getInstance();

        editTextLoginEmail =findViewById(R.id.editText_parent_login_email);
        editTextLoginPwd=findViewById(R.id.editText_parent_login_password);

        Button buttonidPassword = findViewById(R.id.button_parent_login);
        buttonidPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail =editTextLoginEmail.getText().toString();
                String textPwd =editTextLoginPwd.getText().toString();

                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(School_Input_Password.this,"Please enter Your EmailId",Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("Email is Required");
                    editTextLoginEmail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(School_Input_Password.this,"Please re-enter Your EmailId",Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("Valid Email is required");
                    editTextLoginEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(School_Input_Password.this,"Please enter Your Password",Toast.LENGTH_LONG).show();
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
                    Toast.makeText(School_Input_Password.this,"User is identified.",Toast.LENGTH_LONG).show();

                    startActivity(new Intent(School_Input_Password.this,SchoolChooseWay.class));
                    finish();
                    //Get instance of the current User
                    FirebaseUser firebaseUser=authProfile.getCurrentUser();

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
                        Toast.makeText(School_Input_Password.this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}