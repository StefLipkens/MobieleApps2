package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference("drinks");
    Drinks drink;
    List<Drinks> drinksList;
    TextView drinkNameTxtView, drinkPriceTxtView;

    ListView drinksListView;
    Button addBtn;

    String selectedDrinkKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_detail);

        drinksListView=(ListView) findViewById(R.id.drinkListView);
        drinksListView.setOnItemClickListener(this);

        drinkNameTxtView=(TextView)findViewById(R.id.DrinkName);
        drinkPriceTxtView=(TextView)findViewById(R.id.DrinkPrice);

        addBtn=(Button)findViewById(R.id.AddBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double price = Double.valueOf(drinkPriceTxtView.getText().toString()).doubleValue();

            }
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

    public void getData(DataSnapshot dataSnapshot)
    {
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

        drinkNameTxtView.setText(selectedDrink.getName());
        drinkPriceTxtView.setText(selectedDrink.getPrice());
    }
}