package com.example.studentescortsystem.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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


import com.example.studentescortsystem.MainActivity;
import com.example.studentescortsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button buttonPwdReset;
    private EditText editTextResetPwdEmail;
    private ProgressBar progressbar;
    private FirebaseAuth authProfile;

    private final static String TAG = "ForgotPasswordActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().setTitle("Forgot Password");
        editTextResetPwdEmail =findViewById(R.id.editText_password_reset_email);
        buttonPwdReset =findViewById(R.id.button_password_reset);
        progressbar =findViewById(R.id.progressBar);


        buttonPwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email =editTextResetPwdEmail.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(ForgotPasswordActivity.this,"Please enter your register Email",Toast.LENGTH_LONG).show();
                    editTextResetPwdEmail.setError("email is required");
                    editTextResetPwdEmail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(ForgotPasswordActivity.this,"Please enter valid Email",Toast.LENGTH_LONG).show();
                    editTextResetPwdEmail.setError(" Valid email is required");
                    editTextResetPwdEmail.requestFocus();
                }else{
                    progressbar.setVisibility(View.VISIBLE);
                    resetPassword(email);
                }
            }
        });

    }

    private void resetPassword(String email) {
        authProfile =FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPasswordActivity.this,"Please check your inbox for password reset link",Toast.LENGTH_LONG).show();

                    Intent intent =new Intent(ForgotPasswordActivity.this, MainActivity.class);

                    //clear stack to prevent user coming back to  ForgotPasswordActivity
                    intent.setFlags((Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();//close Userprofileactivity
                }else {
                    try{
                        throw task.getException();

                    }catch (FirebaseAuthInvalidCredentialsException e){
                        editTextResetPwdEmail.setError("User does not exists or is no longer valid.Please register again.");
                    }
                    catch (Exception e) {
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(ForgotPasswordActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                progressbar.setVisibility(View.GONE);
            }
        });
    }
}