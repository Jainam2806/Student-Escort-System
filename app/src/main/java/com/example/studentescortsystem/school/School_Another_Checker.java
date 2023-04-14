package com.example.studentescortsystem.school;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.studentescortsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Map;

public class School_Another_Checker extends AppCompatActivity {
    private EditText editTextStudentName, editTextTakerName, editTextParentMobile;
    public String textStudentName;
    public String textTakerName;
    public String textMobile;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private static final String TAG = "Another_Checker";
    Button btn_scan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_another_checker);

        getSupportActionBar().setTitle("Enter ID_Password");

        progressBar = findViewById(R.id.progressBar);
        editTextStudentName = findViewById(R.id.editText_parent_student_name);
        editTextTakerName = findViewById(R.id.editText_parent_taker_name);
        editTextParentMobile = findViewById(R.id.editText_parent_mobile_number);

        btn_scan = findViewById(R.id.button_taker_submit);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textStudentName = editTextStudentName.getText().toString();
                textTakerName = editTextTakerName.getText().toString();
                textMobile = editTextParentMobile.getText().toString();
                FirebaseFirestore.getInstance().collection("Parent").document("Student Details").collection(textStudentName)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                boolean studentFound = false;
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    // Fetch the data here and do something with it
                                    Map<String, Object> data = document.getData();
                                    // Access specific fields by key
                                    String field1 = document.getString("Mobile");
                                    if (field1.equals(textMobile)) {
                                        studentFound = true;
                                        Toast.makeText(School_Another_Checker.this, field1, Toast.LENGTH_SHORT).show();
                                        // Do something with the fetched data
                                    } else {
                                        Toast.makeText(School_Another_Checker.this, "Student not Found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                if (!studentFound) {
                                    Toast.makeText(School_Another_Checker.this, "Student not Found", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(School_Another_Checker.this, "Student not Found", Toast.LENGTH_SHORT).show();
                            }
                        });

                // Check for SMS permissions
                if (ContextCompat.checkSelfPermission(School_Another_Checker.this, Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Request SMS permissions if not granted
                    ActivityCompat.requestPermissions(School_Another_Checker.this, new String[]{Manifest.permission.SEND_SMS}, 1);
                } else {
                    // Send SMS message
                    sendSMS(textMobile, textTakerName+"is arrived at school to take your child.IF they are valid person then write back to OK."); // Replace "Your SMS message" with your actual SMS message
                }
            }
        });
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error sending SMS: " + e.getMessage());
            e.printStackTrace();
        }
    }
}