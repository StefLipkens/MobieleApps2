package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference preRef = db.getReference("customers");
    ArrayList<HistoryItem> historyItemList;
    HistoryItem historyItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        preRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    public void getData(DataSnapshot dataSnapshot)
    {
        historyItemList = new ArrayList<>();
//        ArrayList<HashMap<String, String>> data =
//                new ArrayList<HashMap<String, String>>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            //HashMap<String, String> map = new HashMap<String, String>();
            String name = ds.child("name").getValue().toString();
            String price = ds.child("price").getValue().toString();
            String id = ds.child("id").getValue().toString();
//            Log.d("key", ds.getKey());
//            map.put("description", id);
//            map.put("name", name);
//            map.put("price", price);
//            data.add(map);

           /* historyItem = new HistoryItem(name, price,id);
            historyItemList.add(historyItem);*/
        }

        // create the resource, from, and to variables
        /*int resource = R.layout.drinks_row;
        String[] from = {"name", "price"};
        int[] to = {R.id.nameTextView, R.id.priceTextView};

        // create and set the adapter
        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
//        drinksListView.setAdapter(adapter);*/
    }

}
