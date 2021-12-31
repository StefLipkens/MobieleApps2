package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class AddUserActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference("customers");

    Button addCustomerButton;
    EditText mEditName,mEditTelNr;

    Long nextId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        mEditName = (EditText)findViewById(R.id.editCustomerName);
        mEditTelNr = (EditText)findViewById(R.id.editCustomerTelNr);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                nextId=dataSnapshot.getChildrenCount()+1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addCustomerButton=(Button)findViewById(R.id.addCustomerBtn);
        addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mEditName.getText().toString();
                String telNr = mEditTelNr.getText().toString();

                NewCustomer(name, telNr);
            }
        });
    }

    public void NewCustomer(String name, String telNr)
    {
        Date date = Calendar.getInstance().getTime();
        String lastTimePaid = date.toString();
        String NewId = nextId.toString();
        String status = "0";
        Customer customer = new Customer(NewId,name, telNr,status, lastTimePaid);
        myRef.child(NewId).setValue(customer);
        myRef.child(NewId).setValue(customer)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(),"Customer added!",Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Write failed
                    // ...
                }
            });
        finish();
    }



}