package com.clicknshop.goshopadmin.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.clicknshop.goshopadmin.Models.Customer;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;
import com.clicknshop.goshopadmin.Utils.NotificationAsync;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SendNotifications extends AppCompatActivity {
    EditText notificationMessage, notificationTitle;
    Button send;
    DatabaseReference mDatabase;
    ArrayList<String> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notifications);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Send Notification");
        notificationMessage = findViewById(R.id.notificationMessage);
        notificationTitle = findViewById(R.id.notificationTitle);
        send = findViewById(R.id.send);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child("customers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Customer customer = snapshot.getValue(Customer.class);
                        if (customer != null) {
                            arrayList.add(customer.getFcmKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificationTitle.getText().length() == 0) {
                    notificationTitle.setError("Enter Title");
                } else if (notificationMessage.getText().length() == 0) {
                    notificationMessage.setError("Enter Message");
                } else if (arrayList.size() == 0) {
                    CommonUtils.showToast("No Users");

                } else {

                    for (String keys : arrayList) {
                        sendNotification(keys);
                    }
                    CommonUtils.showToast("Sent notification to all");
                    finish();
                }
            }
        });
    }

    private void sendNotification(String keys) {
        NotificationAsync notificationAsync = new NotificationAsync(SendNotifications.this);
        String notification_title = notificationTitle.getText().toString();
        String notification_message = notificationMessage.getText().toString();
        notificationAsync.execute("ali", keys, notification_title, notification_message, "marketing", "abc");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
