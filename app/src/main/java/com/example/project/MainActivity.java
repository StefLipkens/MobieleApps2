package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    //private Button button;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference("customers");
    Customer customer;
    List<Customer> customerList;
    ListView customerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customerListView=(ListView)findViewById(R.id.customerListView);
        customerListView.setOnItemClickListener(this);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                getData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

    private void getData(DataSnapshot dataSnapshot)
    {
        customerList = new ArrayList<>();
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String name = ds.child("name").getValue().toString();
            String telNr = ds.child("telNr").getValue().toString();
            String id = ds.child("id").getValue().toString();
            String lastTimePaid = ds.child("lastTimePaid").getValue().toString();
            String status = ds.child("status").getValue().toString();

            map.put("name", name);
            map.put("telNr", telNr);
            map.put("status", status);
            map.put("lastTimePaid",lastTimePaid);
            data.add(map);

            customer = new Customer(id,name,telNr,status,lastTimePaid);
            customerList.add(customer);
        }

        // create the resource, from, and to variables
        int resource = R.layout.customers_row;
        String[] from = {"name", "status", "lastTimePaid"};
        int[] to = {R.id.CustomerNameTextView, R.id.CustomerStatusTextView, R.id.lastTimePaidTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        customerListView.setAdapter(adapter);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent userIntent = new Intent(MainActivity.this, CustomerDetailActivity.class);
        startActivity(userIntent);
    }
}