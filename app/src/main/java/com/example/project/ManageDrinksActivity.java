package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.jar.Attributes;


public class ManageDrinksActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference("drinks");

    private Drinks drink;

    private EditText mEditName,mEditPrice;
    private Button addDrinkButton;

    private String selectedDrinkKey;
  //  private int nextId;
    private boolean updating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_drinks);

        ReplaceFragment(new DrinksFragment(ManageDrinksActivity.this));

        mEditName = (EditText)findViewById(R.id.editDrinkName);
        mEditPrice = (EditText)findViewById(R.id.editDrinkPrice);

//        Query HighestId = myRef.child("id").limitToLast(1);
//        HighestId.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot){
//                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
//                    String Key = childSnapshot.getKey();
//                    nextId = Integer.parseInt(Key)+1;
//                    Log.d("nextId",String.valueOf(Key));
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                throw databaseError.toException();
//            }
//        });



        addDrinkButton = (Button)findViewById(R.id.addDrinkBtn);
        addDrinkButton.setOnClickListener(view -> {

            String name = mEditName.getText().toString();
            String price = mEditPrice.getText().toString();
            if(name.isEmpty()||price.isEmpty())
            {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_error_name_price),Toast.LENGTH_LONG).show();
            }
            else
            {
                if (!updating)
                {
                    NewDrink(name, price);
                }
                else
                {
                    UpdateDrink(name,price);
                }
            }
        });
        addDrinkButton.setOnLongClickListener(view -> {

            String name = mEditName.getText().toString();
            String price = mEditPrice.getText().toString();
            if(name.isEmpty()||price.isEmpty())
            {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_error_select_drink),Toast.LENGTH_LONG).show();
            }
            else
            {
                RemoveDrink();
            }
            return true;
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
        selectedDrinkKey = drink.getId();
        updating=true;
        addDrinkButton.setText("update");
        mEditPrice.setText(drink.getPrice());
        mEditName.setText(drink.getName());
    }

    private void RemoveDrink() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.dialog_title_delete_drink));
        builder.setMessage(getString(R.string.dialog_message_delete_confirmation_1) +" "+ drink.getName()+" "+getString(R.string.dialog_message_delete_confirmation_2)+" ?");
        builder.setPositiveButton(getString(R.string.dialog_positive_btn),
                (dialog, which) -> {
                    myRef.child(drink.getId()).removeValue();
                    myRef.child(drink.getId()).removeValue().addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(),getString(R.string.toast_drink_deleted),Toast.LENGTH_SHORT).show());
                    mEditPrice.setText("");
                    mEditName.setText("");
                });
        builder.setNegativeButton(getString(R.string.dialog_negative_btn),
                (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void UpdateDrink(String name, String price) {
        Drinks drink = new Drinks(name, price,selectedDrinkKey);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put( selectedDrinkKey, drink);
        myRef.updateChildren(childUpdates);
        myRef.updateChildren(childUpdates)
                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(),getString(R.string.toast_drink_updated),Toast.LENGTH_SHORT).show());
        //finish();
    }

    private void NewDrink(String name, String price) {

        String NewId = "0";
        Drinks drink = new Drinks(name,price, NewId);

        DatabaseReference newDrinkRef = myRef.push();
        newDrinkRef.setValue(drink);
        newDrinkRef.setValue(drink)
                .addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_drink_added),Toast.LENGTH_SHORT).show());
        //finish();

    }

}

