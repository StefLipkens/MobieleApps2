package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CustomerDetailActivity extends AppCompatActivity implements TextWatcher {


    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myCustomers = db.getReference("customers");
    private DatabaseReference myHistory;

    private Drinks drink;
    private Customer customer;


    private ArrayList<HistoryItem> historyItemArrayList = new ArrayList<>();

    private TextView drinkNameTxtView, drinkPriceTxtView, userNameTextView;
    private EditText descriptionEditText, priceEditText;
    private Button addBtn, checkhistoryBtn, setTo0Btn, paymentRequestBtn;

    private boolean customDrink=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);



        drinkNameTxtView = (TextView) findViewById(R.id.DrinkName);
        drinkPriceTxtView = (TextView) findViewById(R.id.DrinkPrice);
        userNameTextView = (TextView) findViewById(R.id.UserNameTextView);

        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        priceEditText = (EditText) findViewById(R.id.priceEditText);
        descriptionEditText.addTextChangedListener(this);
        priceEditText.addTextChangedListener(this);

        ReplaceFragment(new DrinksFragment(CustomerDetailActivity.this));

        addBtn = (Button) findViewById(R.id.AddBtn);
        addBtn.setOnClickListener(view -> {
            String description;
            String price;
            if(customDrink) {
                description = descriptionEditText.getText().toString();
                price = priceEditText.getText().toString();
                if(description.isEmpty()||price.isEmpty()){
                    Toast.makeText(getApplicationContext(),getString(R.string.toast_error_name_price),Toast.LENGTH_LONG).show();
                }
                else {
                    AddDrink(price,description);
                    Toast.makeText(getApplicationContext(),getString(R.string.toast_drink_added),Toast.LENGTH_SHORT).show();

                }

            }
            else if(drink!=null) {
                description = drink.getName();
                price = drink.getPrice();
                AddDrink(price,description);
                Toast.makeText(getApplicationContext(),getString(R.string.toast_drink_added),Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),getString(R.string.toast_error_select_drink),Toast.LENGTH_SHORT).show();
            }

        });

        checkhistoryBtn = (Button) findViewById(R.id.CheckHistoryBtn);
        checkhistoryBtn.setOnClickListener(view -> {
            Intent history = new Intent(CustomerDetailActivity.this, HistoryActivity.class);
            Bundle b = new Bundle();
            b.putString("key", customer.getId());
            history.putExtras(b);

            startActivity(history);
        });

        setTo0Btn = (Button)findViewById(R.id.SetToZeroBtn);
        setTo0Btn.setOnClickListener(view -> SetTo0());

        paymentRequestBtn = (Button)findViewById(R.id.PaymentRequestBtn);
        paymentRequestBtn.setOnClickListener(view -> {
            if(customer.getStatus()!=null) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(customer.getTelNr(), null, getString(R.string.sms_payment_request_1)+" "+customer.getStatus()+getString(R.string.sms_payment_request_2)+"?", null, null);
                smsManager.sendTextMessage(customer.getTelNr(),null,"BE31 7370 5087 7755",null,null);
                Toast.makeText(getApplicationContext(),getString(R.string.toast_sms_sent),Toast.LENGTH_SHORT).show();

            }
        });

        myCustomers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Bundle b = getIntent().getExtras();
                String customerKey = myCustomers.child(b.getString("key")).getKey();
                customer = dataSnapshot.child(customerKey).getValue(Customer.class);
                customer.setId(customerKey);
                userNameTextView.setText(customer.getName() + " - " + customer.getStatus());
                if(Double.valueOf(customer.getStatus())>0)
                {
                    paymentRequestBtn.setVisibility(View.VISIBLE);
                    checkhistoryBtn.setVisibility(View.VISIBLE);
                    setTo0Btn.setVisibility(View.VISIBLE);
                }

                Log.d("customerkey",customer.getId());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ReplaceFragment(DrinksFragment drinksFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.drinksFrameLayout,drinksFragment);
        fragmentTransaction.commit();
    }

    public void SetDrink(Drinks drink) {
        this.drink=drink;
        customDrink=false;
        drinkNameTxtView.setText(drink.getName());
        drinkPriceTxtView.setText(drink.getPrice());
    }

    private void AddDrink(String price, String description) {
        myHistory = myCustomers.child(customer.getId()).child("history");
        if (customer.getStatus().equals("0"))
        {
            myHistory.setValue(historyItemArrayList);
        }
        else
        {
            historyItemArrayList = customer.getHistory();
        }

        String currentStatus = customer.getStatus();
        Double doubleStatus = Double.valueOf(currentStatus);
        Double doublePrice = Double.valueOf(price);
        Double doubleTotal = doublePrice + doubleStatus;
        String newStatus = String.valueOf(doubleTotal);
        Date date = Calendar.getInstance().getTime();
        String dateTime = DateFormat.getDateTimeInstance().format(date);
        HistoryItem historyItem = new HistoryItem(price, description, dateTime);

        historyItemArrayList.add(historyItem);

        customer.setLastTimeOrdered(dateTime);
        customer.setStatus(newStatus);
        customer.setHistory(historyItemArrayList);

        myCustomers.child(customer.getId()).setValue(customer);
        finish();
    }

    private void SetTo0() {
        customer.getName();
        customer.getId();
        Date date = Calendar.getInstance().getTime();
        String dateTime = DateFormat.getDateTimeInstance().format(date);
        customer.setLastTimePaid(dateTime);
        customer.setStatus("0");
        historyItemArrayList.clear();
        customer.setHistory(historyItemArrayList);
        myCustomers.child(customer.getId()).setValue(customer);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        drinkNameTxtView.setText("");
        drinkPriceTxtView.setText("");
        customDrink=true;
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}