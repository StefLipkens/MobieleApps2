package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<HistoryItem> {
    public HistoryAdapter(@NonNull Context context, ArrayList<HistoryItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        HistoryItem item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_item_row, parent, false);
        }
        // Lookup view for data population
        TextView itemName = (TextView) convertView.findViewById(R.id.descriptionTextView);
        TextView itemPrice = (TextView) convertView.findViewById(R.id.priceTextView);
        TextView itemAddedDate = (TextView) convertView.findViewById(R.id.dateTimeTextView);
        // Populate the data into the template view using the data object
        itemName.setText(item.getDescription());
        itemPrice.setText(item.getPrice());
        itemAddedDate.setText(item.getOrderDateTime().toString());
        // Return the completed view to render on screen
        return convertView;
    }
}
