package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myDrinks = db.getReference("drinks");
    private DatabaseReference myCustomers = db.getReference("customers");
    private DatabaseReference myHistory;

    Drinks drink;
    Customer customer;

    ArrayList<Drinks> drinksList;
    ArrayList<HistoryItem> historyItemArrayList = new ArrayList<>();

    TextView drinkNameTxtView, drinkPriceTxtView, userNameTextView;
    EditText descriptionEditText;

    ListView drinksListView;
    Button addBtn, checkhistoryBtn;

    String nextId;
    String snapShotCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        drinksListView=(ListView) findViewById(R.id.drinkListView);
        drinksListView.setOnItemClickListener(this);

        drinkNameTxtView=(TextView)findViewById(R.id.DrinkName);
        drinkPriceTxtView=(TextView)findViewById(R.id.DrinkPrice);
        userNameTextView=(TextView)findViewById(R.id.UserNameTextView);

        descriptionEditText=(EditText)findViewById(R.id.descriptionEditText);

        addBtn=(Button)findViewById(R.id.AddBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddDrink();

            }
        });

        checkhistoryBtn=(Button)findViewById(R.id.CheckHistoryBtn);
        checkhistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent history = new Intent(CustomerDetailActivity.this, HistoryActivity.class);
                startActivity(history);
            }
        });

        myDrinks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                drinksList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue().toString();
                    String price = ds.child("price").getValue().toString();
                    String id = ds.child("id").getValue().toString();
                    drink = new Drinks(name, price,id);
                    drinksList.add(drink);
                }

                DrinkAdapter drinkadapter =
                        new DrinkAdapter(CustomerDetailActivity.this,drinksList);
                drinksListView.setAdapter(drinkadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myCustomers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                Bundle b = getIntent().getExtras();
                String customerKey = myCustomers.child(b.getString("key")).getKey();
                customer = dataSnapshot.child(customerKey).getValue(Customer.class);
//                historyItemArrayList = customer.getHistory();
//                myHistory = myCustomers.child(customer.getId()).child("history");
                Log.d("Current User",customer.getName());
                userNameTextView.setText(customer.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void AddDrink() {

        myHistory = myCustomers.child(customer.getId()).child("history");
        if(customer.getStatus().equals("0"))
        {
            myHistory.setValue(historyItemArrayList);
        }
        else
        {
            historyItemArrayList=customer.getHistory();
        }

        //VOLGENDE STAP: SET TO 0 BUTTON MAKEN DIE CHILD("history") VERWIJDERT
        String total=customer.getStatus();
        Double doubleTotal = Double.valueOf(total);
        String price =drink.getPrice();
        Double doublePrice = Double.valueOf(price);
        String newStatus = String.valueOf(doublePrice+doubleTotal);
        String description = descriptionEditText.getText().toString();
        Log.d("sdf",description);
        if(description.equals("Description") || description == "" || description.isEmpty())
        {
            description=drink.getName();
        }
        Log.d("sdfff",description);
        String date = Calendar.getInstance().getTime().toString();


        HistoryItem historyItem = new HistoryItem(drink.getPrice(), description, date);

        historyItemArrayList.add(historyItem);

        customer.setLastTimePaid(date);
        customer.setStatus(newStatus);
        customer.setHistory(historyItemArrayList);

        myCustomers.child(customer.getId()).setValue(customer);
//        myCustomers.child(customer.getId()).child("history").setValue(historyItemArrayList);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        drink = (Drinks)drinksListView.getItemAtPosition(i);
        drinkNameTxtView.setText(drink.getName());
        drinkPriceTxtView.setText(drink.getPrice());
    }
}