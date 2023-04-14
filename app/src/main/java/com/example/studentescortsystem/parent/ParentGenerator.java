package com.example.studentescortsystem.parent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentescortsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Locale;

public class ParentGenerator extends AppCompatActivity {

    private ProgressBar progressBar;
    private String fullName,rollNo,school, email, doB, gender, mobile;
    private ImageView imageView;
    Button btGenerate;
    private FirebaseAuth authProfile;
    private static final long START_TIME_IN_MILLS = 60000;

    private TextView mTextViewCountDown;
    private Button mButtonNow;
    private CountDownTimer mCountDownTimer;

    private boolean mTimeRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_generator);

        getSupportActionBar().setTitle("GenerateQrCode");

        //assign Variable
        mTextViewCountDown = findViewById(R.id.parent_generate_countdown);
        btGenerate = findViewById(R.id.parent_generate_now);
        imageView = findViewById(R.id.parent_qrcode);


        btGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Time

                mCountDownTimer =new CountDownTimer(mTimeLeftInMillis,1000){

                    @Override
                    public void onTick(long millisUntilFinished) {
                        mTimeLeftInMillis=millisUntilFinished;
                        int minutes =(int)(mTimeLeftInMillis /1000/60);
                        int seconds =(int)(mTimeLeftInMillis /1000)%60;

                        String timeleftFormatted=String.format(Locale.getDefault(),"%02d:%02d",minutes,seconds);
                        mTextViewCountDown.setText(timeleftFormatted);


                        //initialize multi formate writer

                        authProfile = FirebaseAuth.getInstance();
                        FirebaseUser firebaseUser = authProfile.getCurrentUser();
                        String userID = firebaseUser.getUid();

                        //Extracting User reference from Database for "Registered User"
                        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered users");
                        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                                if (readUserDetails != null) {
                                    fullName = firebaseUser.getDisplayName();
                                    email = firebaseUser.getEmail();
                                    school=readUserDetails.school;
                                    rollNo=readUserDetails.rollNo;
                                    doB = readUserDetails.doB;
                                    gender = readUserDetails.gender;
                                    mobile = readUserDetails.mobile;

                                    String text =  fullName ;
//                                            + "\n" + doB+"\n" + rollNo + "\n" + mobile;
                                    MultiFormatWriter writer = new MultiFormatWriter();

                                    //Initialize bit matrix
                                    try {
                                        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 350, 350);

                                        BarcodeEncoder encoder = new BarcodeEncoder();

                                        //Initialize bitmap
                                        Bitmap bitmap = encoder.createBitmap(matrix);

                                        //ser bitmap on image view

                                        imageView.setImageBitmap(bitmap);

                                        //Initialize input manager
                                        InputMethodManager manager = (InputMethodManager) getSystemService(
                                                Context.INPUT_METHOD_SERVICE
                                        );

                                    } catch (WriterException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(ParentGenerator.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(ParentGenerator.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onFinish() {
                        Toast.makeText(ParentGenerator.this, "Time Out.If not Escorted!Try another Way. ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ParentGenerator.this,ParentUserProfile.class);
                        startActivities(new Intent[]{intent});
                    }
                }.start();
                mTimeRunning=true;
            }
        });

    }
}