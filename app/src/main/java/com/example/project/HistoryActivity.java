package com.example.project;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = db.getReference("customers");

    private Customer customer;
    private ArrayList<HistoryItem> historyItemList;

    private ListView historyListView;
    private TextView userTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyListView = (ListView)findViewById(R.id.historyItemListView);
        userTextView=(TextView)findViewById(R.id.userNameTextView);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("datasnapshot", dataSnapshot.toString());
                Bundle b = getIntent().getExtras();
                String customerKey = myRef.child(b.getString("key")).getKey();
                customer = dataSnapshot.child(customerKey).getValue(Customer.class);
                userTextView.setText(getString(R.string.lbl_history)+" - "+ customer.getName());
                historyItemList=customer.getHistory();
                if(historyItemList==null) {
                    Log.d("key","sdf");
                    AlertDialog.Builder builder = new AlertDialog.Builder(HistoryActivity.this);
                    builder.setTitle(getResources().getString(R.string.dialog_title_no_history));
                    builder.setMessage(getResources().getString(R.string.dialog_message_no_history));
                    builder.setPositiveButton(getResources().getString(R.string.dialog_btn_no_history), (dialog, which) -> finish());
                    AlertDialog dialog = builder.create();
                    //dialog.show();                                //PROBLEEM TREEDT HIER OP
                }
                else {
                    HistoryAdapter historyAdapter =
                            new HistoryAdapter(HistoryActivity.this, historyItemList);
                    historyListView.setAdapter(historyAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
