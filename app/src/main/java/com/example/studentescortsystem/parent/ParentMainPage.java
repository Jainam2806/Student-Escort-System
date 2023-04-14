package com.example.studentescortsystem.parent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.studentescortsystem.R;

public class ParentMainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_main_page);

        //set the title
        getSupportActionBar().setTitle("Parent Main");

        Button buttonParentsLogin = findViewById(R.id.button_parents_login);
        buttonParentsLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentMainPage.this, ParentLogin.class);
                startActivities(new Intent[]{intent});
            }

        });

        Button buttonSchoolRegister = findViewById(R.id.button_parents_register);
        buttonSchoolRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ParentMainPage.this, ParentRegister.class);
                startActivities(new Intent[]{intent});
            }


        });
    }
}