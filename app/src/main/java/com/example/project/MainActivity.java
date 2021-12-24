package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addUserButton = findViewById(R.id.newUserBtn);
        addUserButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddUserActivity.class);
                startActivity(intent);
            }
        });

        Button addDrinkButton = findViewById(R.id.newDrinkBtn);
        addDrinkButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddDrinkActivity.class);
                startActivity(intent);
            }
        });
    }
}