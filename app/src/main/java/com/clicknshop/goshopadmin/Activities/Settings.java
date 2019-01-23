package com.clicknshop.goshopadmin.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.clicknshop.goshopadmin.Activities.Categories.AddCategories;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Settings extends AppCompatActivity {

    EditText deliveryCharges, adminNumber, textMessage;
    Button updateDelivery, btnAdminNumber, btnMessage,bannerSettings,categories;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Settings");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        deliveryCharges = findViewById(R.id.deliveryCharges);
        adminNumber = findViewById(R.id.adminNumber);
        textMessage = findViewById(R.id.textMessage);
        updateDelivery = findViewById(R.id.updateDelivery);
        btnAdminNumber = findViewById(R.id.btnAdminNumber);
        btnMessage = findViewById(R.id.btnMessage);
        bannerSettings = findViewById(R.id.bannerSettings);
        categories = findViewById(R.id.categories);


        getDataFromServer();


        categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Settings.this,AddCategories.class);
                startActivity(i);
            }
        });


        updateDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deliveryCharges.getText().length() == 0) {
                    deliveryCharges.setError("Enter Value");
                } else {
                    mDatabase.child("Settings").child("DeliveryCharges").setValue(Integer.parseInt(deliveryCharges.getText().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CommonUtils.showToast("Updated");
                        }
                    });
                }
            }
        });
        btnAdminNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminNumber.getText().length() == 0) {
                    adminNumber.setError("Enter Value");
                } else {
                    mDatabase.child("Settings").child("AdminNumber").setValue(adminNumber.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CommonUtils.showToast("Updated");
                        }
                    });
                }
            }
        });
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textMessage.getText().length() == 0) {
                    textMessage.setError("Enter Text");
                } else {
                    mDatabase.child("Settings").child("TextMessage").setValue(textMessage.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CommonUtils.showToast("Updated");
                        }
                    });
                }

            }
        });

        bannerSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Settings.this,BannerSettings.class);
                startActivity(i);
            }
        });


    }

    private void getDataFromServer() {
        mDatabase.child("Settings").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    deliveryCharges.setText("" + dataSnapshot.child("DeliveryCharges").getValue(Integer.class));
                    adminNumber.setText(dataSnapshot.child("AdminNumber").getValue(String.class));
                    textMessage.setText(dataSnapshot.child("TextMessage").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
