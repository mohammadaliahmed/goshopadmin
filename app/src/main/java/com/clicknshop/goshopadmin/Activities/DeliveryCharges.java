package com.clicknshop.goshopadmin.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeliveryCharges extends AppCompatActivity {
    DatabaseReference mDatabase;
    EditText charges;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_charges);
        charges = findViewById(R.id.charges);
        update = findViewById(R.id.update);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (charges.getText().length() == 0) {
                    charges.setError("Enter value");
                } else {
                    putChargesToDB();
                }
            }
        });
        getDataFromDB();
    }

    private void getDataFromDB() {
        mDatabase.child("Settings").child("DeliveryCharges").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Integer value = dataSnapshot.getValue(Integer.class);
                    charges.setText("" + value);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void putChargesToDB() {
        mDatabase.child("Settings").child("DeliveryCharges").setValue(Integer.parseInt(charges.getText().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Updated");
            }
        });
    }
}
