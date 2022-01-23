package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.Drinks;
import com.example.project.R;

import java.util.ArrayList;

public class DrinkAdapter extends ArrayAdapter<Drinks> {
    public DrinkAdapter(@NonNull Context context, ArrayList<Drinks> drinks) {
        super(context, 0, drinks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Drinks drink = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.drinks_row, parent, false);
        }
        // Lookup view for data population
        TextView drinkName = (TextView) convertView.findViewById(R.id.nameTextView);
        TextView drinkPrice = (TextView) convertView.findViewById(R.id.priceTextView);
        // Populate the data into the template view using the data object
       drinkName.setText(drink.getName());
       drinkPrice.setText("€ "+drink.getPrice());
        // Return the completed view to render on screen
        return convertView;
    }
}
