package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference("customers");
    private Customer customer;
    private ArrayList<Customer> customerList;
    private ListView customerListView;
    private TextView totalTxtView;
    private Double total=0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        totalTxtView=(TextView)findViewById(R.id.TotalTxtView);


        customerListView=(ListView)findViewById(R.id.customerListView);
        customerListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent customerDetail = new Intent(MainActivity.this, CustomerDetailActivity.class);
            customer = (Customer) customerListView.getItemAtPosition(i);
            Bundle b = new Bundle();
            b.putString("key", customer.getId());
            customerDetail.putExtras(b);
            //Log.d("customerkey",customer.getId());
            startActivity(customerDetail);
        });
        customerListView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            customer = (Customer) customerListView.getItemAtPosition(i);
            RemoveCustomer();
            return true;
        });

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
    }

    private void RemoveCustomer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.dialog_title_delete_customer));
        builder.setMessage(getString(R.string.dialog_message_delete_confirmation_1) + customer.getName() + getString(R.string.dialog_message_delete_confirmation_2) +" ?");
        builder.setPositiveButton(getString(R.string.dialog_positive_btn),
                (dialog, which) -> {
                    myRef.child(customer.getId()).removeValue();
                    myRef.child(customer.getId()).removeValue().addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(),getString(R.string.toast_customer_deleted),Toast.LENGTH_SHORT).show());
                });

        builder.setNegativeButton(getString(R.string.dialog_negative_btn),
                (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getData(DataSnapshot dataSnapshot) {
        total=0.0;
        customerList = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            String name = ds.child("name").getValue().toString();
            String telNr = ds.child("telNr").getValue().toString();
            String id = ds.getKey();
            String lastTimePaid = ds.child("lastTimePaid").getValue().toString();
            String lastTimeOrdered = ds.child("lastTimeOrdered").getValue().toString();
            String status = ds.child("status").getValue().toString();
            ArrayList<HistoryItem> historyItemArrayList=new ArrayList<>();
            Double dblStatus = Double.valueOf(status);
            total +=dblStatus;
            for(DataSnapshot dsi : ds.child("history").getChildren())
            {
                HistoryItem item = new HistoryItem(dsi.child("description").getValue().toString(),dsi.child("orderDateTime").getValue().toString(), dsi.child("price").getValue().toString());
                historyItemArrayList.add(item);
            }

            customer = new Customer(id,name,telNr,status,lastTimePaid,lastTimeOrdered,historyItemArrayList);
            customerList.add(customer);
        }
        totalTxtView.setText("€ "+String.valueOf(total));
        CustomerAdapter customerAdapter = new CustomerAdapter(this, customerList);
        customerListView.setAdapter(customerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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