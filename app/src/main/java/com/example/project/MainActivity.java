package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Button addUserButton = findViewById(R.id.newUserBtn);
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
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.addUserItem:
                Intent userIntent = new Intent(MainActivity.this, AddUserActivity.class);
                startActivity(userIntent);
                return true;
            case R.id.addDrinkItem:
                Intent drinkIntent = new Intent(MainActivity.this, ManageDrinksActivity.class);
                startActivity(drinkIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}