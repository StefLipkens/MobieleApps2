package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.jar.Attributes;


public class ManageDrinksActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference("drinks");
    Drinks drink;
    List<Drinks> drinksList;
    EditText mEditName,mEditPrice;
    Button addDrinkButton;

    Boolean editPriceBln;

    ListView drinksListView;

    String selectedDrinkKey;
    Long nextId;
    String TAG = "tag:";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_drinks);

        drinksListView=(ListView) findViewById(R.id.drinkListView);
        drinksListView.setOnItemClickListener(this);

        mEditName = (EditText)findViewById(R.id.editDrinkName);
        mEditPrice = (EditText)findViewById(R.id.editDrinkPrice);

        editPriceBln=false;

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                getData(dataSnapshot);
                nextId=dataSnapshot.getChildrenCount()+1;
                Log.i("long id",nextId+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        addDrinkButton = (Button)findViewById(R.id.addDrinkBtn);
        addDrinkButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {


                String name = mEditName.getText().toString();
                String price = mEditPrice.getText().toString();
                //Double price = Double.valueOf(mEditPrice.getText().toString()).doubleValue();
                if(!editPriceBln)
                {
                    NewDrink(name, price);
                }
                else
                {
                    UpdateDrink(name,price);
                }

            }
        });

    }

    public void UpdateDrink(String name, String price)
    {
        Drinks drink = new Drinks(name, price,selectedDrinkKey);
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put( selectedDrinkKey, drink);
        myRef.updateChildren(childUpdates);
    }

    public void NewDrink(String name, String price) {

        String NewId = nextId.toString();
        Drinks drink = new Drinks(name,price, NewId);
        Log.d(TAG, name + price + NewId);
        drinksList.add(drink);

       /* drink.setName(name);
        drink.setPrice(price);*/
        myRef.child(NewId).setValue(drink);
    }

    public void getData(DataSnapshot dataSnapshot)
    {
/*
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {
            String name = ds.child("Name").getValue().toString();
            String price = ds.child("Price").getValue().toString();
            //Double price = Double.valueOf(ds.child("Price").getValue().toString()).doubleValue();

            Drinks drink = new Drinks();
            drink.setName(name);
            drink.setPrice(price);

            drinksList.add(drink);

        }*/

        drinksList = new ArrayList<>();
        ArrayList<HashMap<String, String>> data =
                new ArrayList<HashMap<String, String>>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            HashMap<String, String> map = new HashMap<String, String>();
            String name = ds.child("name").getValue().toString();
            String price = ds.child("price").getValue().toString();
            String id = ds.child("id").getValue().toString();
            Log.d("key", ds.getKey());

            map.put("name", name);
            map.put("price", price);
            data.add(map);

            drink = new Drinks(name, price,id);
            drinksList.add(drink);
        }

        // create the resource, from, and to variables
        int resource = R.layout.drinks_row;
        String[] from = {"name", "price"};
        int[] to = {R.id.nameTextView, R.id.priceTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        drinksListView.setAdapter(adapter);


    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Drinks selectedDrink = new Drinks(drinksList.get(i).getName(), drinksList.get(i).getPrice(), drinksList.get(i).getId());
        selectedDrinkKey = drinksList.get(i).getId();
        Log.d(TAG, selectedDrinkKey);
        editPriceBln=true;
        addDrinkButton.setText("Update");
        mEditPrice.setText(selectedDrink.getPrice());
        mEditName.setText(selectedDrink.getName());

    }
}

