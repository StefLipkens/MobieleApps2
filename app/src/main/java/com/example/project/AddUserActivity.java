package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class AddUserActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference("customers");

    private Button addCustomerButton;
    private EditText mEditName,mEditTelNr;

    private ArrayList<HistoryItem> historyItemArrayList =new ArrayList<>();

//    private int nextId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                nextId=0;
//                for(DataSnapshot childSnapshot : snapshot.getChildren())
//                {
//                    nextId++;
//                }
//                Log.d("nextId",String.valueOf(nextId));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        mEditName = (EditText)findViewById(R.id.editCustomerName);
        mEditTelNr = (EditText)findViewById(R.id.editCustomerTelNr);
        addCustomerButton=(Button)findViewById(R.id.addCustomerBtn);
        addCustomerButton.setOnClickListener(view -> {
            String name = mEditName.getText().toString();
            String telNr = mEditTelNr.getText().toString();
            if(name.isEmpty()||telNr.isEmpty())
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_error_name_tel),Toast.LENGTH_LONG).show();
            else
                NewCustomer(name, telNr);
        });
    }

    private void NewCustomer(String name, String telNr) {
        String lastTimePaid = "Nothing paid yet";
        String lastTimeOrdered = "Nothing ordered yet";
        String NewId = "0";
        String status = "0";
        Customer customer = new Customer(NewId,name, telNr,status, lastTimePaid,lastTimeOrdered,historyItemArrayList);



        DatabaseReference newPageRef = myRef.push();
        newPageRef.setValue(customer);

//        myRef.child(NewId).setValue(customer);
//        myRef.child(NewId).setValue(customer)
//            .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(),getString(R.string.toast_customer_added),Toast.LENGTH_SHORT).show());
        finish();
    }

}