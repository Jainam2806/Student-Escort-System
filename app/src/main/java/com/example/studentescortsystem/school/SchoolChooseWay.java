package com.example.studentescortsystem.school;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentescortsystem.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Map;

public class SchoolChooseWay extends AppCompatActivity  {

    private ImageView imageView;
    public  String result1;
    Button btn_scan;
    private String fullname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_choose_way);


        getSupportActionBar().setTitle("Choose Way");

        Button buttonQrScanner = findViewById(R.id.button_choose_scanner);
        buttonQrScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SchoolChooseWay.this, SchoolQrScanner.class);
//                startActivities(new Intent[]{intent});
            }

        });

        Button buttonidPassword = findViewById(R.id.button_choose_idPassword);
        buttonidPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SchoolChooseWay.this, School_Input_Password.class);
                startActivities(new Intent[]{intent});
            }


        });

        Button buttonAnother = findViewById(R.id.button_choose_Another);
        buttonAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SchoolChooseWay.this, School_Another_Checker.class);
                startActivities(new Intent[]{intent});
            }

        });


        btn_scan =findViewById(R.id.button_choose_scanner);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();
            }
        });
    }
    private void scanCode() {

        ScanOptions options = new ScanOptions();
        options.setCameraId(0);
        options.setPrompt("Volume Up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            String studentName = result.getContents();

            FirebaseFirestore.getInstance().collection("Parent").document("Student Details").collection(studentName)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            boolean studentFound = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                // Fetch the data here and do something with it
                                Map<String, Object> data = document.getData();
                                // Access specific fields by key
                                String field1 = document.getString("Name");
                                if (field1.equals(studentName)) {
                                    studentFound = true;
                                    Toast.makeText(SchoolChooseWay.this, "Student Found", Toast.LENGTH_SHORT).show();
                                    // Do something with the fetched data
                                }
                                else{
                                    Toast.makeText(SchoolChooseWay.this, "Student not Found", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (!studentFound) {
                                Toast.makeText(SchoolChooseWay.this, "Student not Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(SchoolChooseWay.this, "Student not Found", Toast.LENGTH_SHORT).show();
                        }
                    });
            AlertDialog.Builder builder = new AlertDialog.Builder(SchoolChooseWay.this);
//            builder.setTitle("Result");
//            builder.setMessage(result.getContents());
//            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss()).show();

        }
        else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
            Toast.makeText(SchoolChooseWay.this, "Student not Found", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(SchoolChooseWay.this);
        }
    });


}