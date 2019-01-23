package com.clicknshop.goshopadmin.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicknshop.goshopadmin.Models.Product;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProduct extends AppCompatActivity {
    String productId;
    DatabaseReference mDatabaseReference;
    ImageView image;
    Button update;
    EditText e_title, e_price, e_subtitle, e_costPrice, e_oldPrice;
    TextView category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Intent i = getIntent();
        productId = i.getStringExtra("productId");

        image = findViewById(R.id.image);

        e_title = findViewById(R.id.title);
        e_price = findViewById(R.id.price);
        e_costPrice = findViewById(R.id.costPrice);
        e_oldPrice = findViewById(R.id.oldPrice);
        e_subtitle = findViewById(R.id.subtitle);
        update = findViewById(R.id.update);
        category = findViewById(R.id.category);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products");

        mDatabaseReference.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        EditProduct.this.setTitle("Edit: " + product.getTitle());
                        category.setText("Category: " + product.getCategory() +
                                ( product.getBrand() != null ? "\nBrand: " + product.getBrand() : "")
                        );
                        e_title.setText(product.getTitle());
                        e_price.setText("" + product.getPrice());
                        e_subtitle.setText(product.getSubtitle());
                        e_costPrice.setText("" + product.getCostPrice());
                        e_oldPrice.setText("" + product.getOldPrice());
                        Glide.with(EditProduct.this).load(product.getThumbnailUrl()).into(image);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (e_title.getText().length() != 0 && e_subtitle.getText().length() != 0 && e_price.getText().length() != 0) {
                    mDatabaseReference.child(productId).child("price").setValue(Integer.parseInt(e_price.getText().toString()));
                    mDatabaseReference.child(productId).child("title").setValue(e_title.getText().toString());
                    mDatabaseReference.child(productId).child("costPrice").setValue(Integer.parseInt(e_costPrice.getText().toString()));
                    mDatabaseReference.child(productId).child("oldPrice")
                            .setValue(Integer.parseInt(e_oldPrice.getText().toString().equalsIgnoreCase("") ? "0" : e_oldPrice.getText().toString()));
                    mDatabaseReference.child(productId).child("title").setValue(e_title.getText().toString());
                    mDatabaseReference.child(productId).child("subtitle").setValue(e_subtitle.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            CommonUtils.showToast("Updated");
                            finish();

                        }
                    });


                } else {
                    CommonUtils.showToast("Please enter values");
                }
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
