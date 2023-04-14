package com.example.studentescortsystem.school;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.studentescortsystem.R;

public class SchoolMainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_main_page);

        //set the title
        getSupportActionBar().setTitle("School Main");

        Button buttonParentsLogin = findViewById(R.id.button_school_login);
        buttonParentsLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SchoolMainPage.this, SchoolLogin.class);
                startActivities(new Intent[]{intent});
            }

        });

        Button buttonSchoolRegister = findViewById(R.id.button_school_register);
        buttonSchoolRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SchoolMainPage.this, SchoolRegister.class);
                startActivities(new Intent[]{intent});
            }


        });
    }
}