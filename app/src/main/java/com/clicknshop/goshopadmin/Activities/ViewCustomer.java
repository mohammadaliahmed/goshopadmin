package com.clicknshop.goshopadmin.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.clicknshop.goshopadmin.Models.Customer;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewCustomer extends AppCompatActivity {
    String customerId;
    DatabaseReference mDatabase;
    TextView name, phone, address, orders, since;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();

        phone = findViewById(R.id.phone);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        orders = findViewById(R.id.orders);
        since = findViewById(R.id.since);
        customerId = getIntent().getStringExtra("customerId");

        getDataFromDB();
    }

    private void getDataFromDB() {
        mDatabase.child("customers").child(customerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    if (customer != null) {
                        ViewCustomer.this.setTitle("Customer: " + customer.getName());
                        name.setText(customer.getName());
                        phone.setText(customer.getPhone());
                        address.setText(customer.getAddress());
                        since.setText(CommonUtils.getFormattedDate(customer.getTime()));
                        orders.setText("" + dataSnapshot.child("Orders").getChildrenCount());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
