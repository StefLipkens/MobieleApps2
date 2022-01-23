package com.example.project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.project.Customer;
import com.example.project.R;

import java.util.ArrayList;

public class CustomerAdapter extends ArrayAdapter<Customer> {
    public CustomerAdapter(@NonNull Context context, ArrayList<Customer> customers) {
        super(context, 0, customers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Customer customer = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customers_row, parent, false);
        }
        // Lookup view for data population
        TextView customerName = (TextView) convertView.findViewById(R.id.CustomerNameTextView);
        TextView customerStatus = (TextView) convertView.findViewById(R.id.CustomerStatusTextView);
        TextView customerLTP= (TextView) convertView.findViewById(R.id.LastTimePaidTextView);
        TextView customerLTO = (TextView) convertView.findViewById(R.id.lastTimeOrderedTextView);
        // Populate the data into the template view using the data object
        customerName.setText(customer.getName());
        customerStatus.setText("€ "+customer.getStatus());
        customerLTP.setText(customer.getLastTimePaid());
        customerLTO.setText(customer.getLastTimeOrdered());
        // Return the completed view to render on screen
        return convertView;
    }
}
