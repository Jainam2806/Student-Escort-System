package com.example.studentescortsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.studentescortsystem.parent.ParentMainPage;
import com.example.studentescortsystem.school.SchoolMainPage;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the title
        getSupportActionBar().setTitle("StudentEscort");

        Button buttonParents = findViewById(R.id.button_parents);
        buttonParents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ParentMainPage.class);
                startActivities(new Intent[]{intent});
            }

        });

        Button buttonSchool = findViewById(R.id.button_school);
        buttonSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SchoolMainPage.class);
                startActivities(new Intent[]{intent});
            }


        });

    }
}