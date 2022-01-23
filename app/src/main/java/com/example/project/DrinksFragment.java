package com.example.project;

import android.os.Bundle;;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DrinksFragment extends Fragment {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myDrinks = db.getReference("drinks");
    private AppCompatActivity currentActivity;
    private Drinks drink;

    private ArrayList<Drinks> drinksList;

    private ListView drinksListView;
    public DrinksFragment(AppCompatActivity activity)
    {
        this.currentActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_drinks, container, false);

        drinksListView = (ListView) view.findViewById(R.id.drinkListView);

        drinksListView.setOnItemClickListener((adapterView, view1, i, l) -> {
            drink = (Drinks)drinksListView.getItemAtPosition(i);

            if(currentActivity.getClass()==ManageDrinksActivity.class)
            {
                ((ManageDrinksActivity)getActivity()).SetDrink(drink);
            }
            else if(currentActivity.getClass()==CustomerDetailActivity.class)
            {
                ((CustomerDetailActivity)getActivity()).SetDrink(drink);
            }
        });
        myDrinks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                drinksList = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue().toString();
                    String price = ds.child("price").getValue().toString();
                    String id = ds.getKey();
                    Drinks availableDrinks = new Drinks(name, price, id);
                    drinksList.add(availableDrinks);
                }

                DrinkAdapter drinkadapter =
                        new DrinkAdapter(currentActivity, drinksList);
                drinksListView.setAdapter(drinkadapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        return view;
    }
}