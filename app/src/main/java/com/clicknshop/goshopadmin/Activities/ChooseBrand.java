package com.clicknshop.goshopadmin.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.clicknshop.goshopadmin.Adapters.BrandsListAdapter;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseBrand extends AppCompatActivity {
    RecyclerView recycler;
    EditText brand;
    Button addBrand;
    DatabaseReference mDatabase;
    ArrayList<String> brandNames = new ArrayList<>();
    BrandsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_brand);
        this.setTitle("Choose brand");

        addBrand = findViewById(R.id.addBrand);
        brand = findViewById(R.id.brand);
        recycler = findViewById(R.id.recycler);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        addBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (brand.getText().length() == 0) {
                    CommonUtils.showToast("Enter Name");
                } else {
                    addBrandToDB();
                }
            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new BrandsListAdapter(this, brandNames, new BrandsListAdapter.OptionChosen() {
            @Override
            public void onDeleteClick(String id) {
                deleteBrandFromDB(id);
            }

            @Override
            public void onBrandChosen(String id) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("brand", id);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        recycler.setAdapter(adapter);

        getDataFromDB();

    }

    private void deleteBrandFromDB(String id) {
        mDatabase.child("Brands").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Removed");
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getDataFromDB() {
        mDatabase.child("Brands").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    brandNames.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String name = snapshot.getValue(String.class);
                        if (name != null) {
                            brandNames.add(name);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    brandNames.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addBrandToDB() {
        mDatabase.child("Brands").child(brand.getText().toString()).setValue(brand.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                CommonUtils.showToast("Brand Name Added");
                brand.setText("");
            }
        });
    }
}
