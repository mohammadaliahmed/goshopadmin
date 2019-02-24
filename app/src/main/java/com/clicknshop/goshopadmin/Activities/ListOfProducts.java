package com.clicknshop.goshopadmin.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.clicknshop.goshopadmin.Adapters.ProductsAdapter;
import com.clicknshop.goshopadmin.Models.Product;
import com.clicknshop.goshopadmin.Models.ProductCountModel;
import com.clicknshop.goshopadmin.R;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.clicknshop.goshopadmin.Utils.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListOfProducts extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ArrayList<Product> productArrayList = new ArrayList<>();
    ArrayList<ProductCountModel> userCartProductList = new ArrayList<>();

    ProductsAdapter adapter;
    String category;
    DatabaseReference mDatabase;
    ProgressBar progress;
    String from;
    int abc = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_products);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("List of products");
        from = getIntent().getStringExtra("from");
        if (from!=null&&from.equalsIgnoreCase("notificationScreen")) {
            abc = 1;
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.recycler_view_products);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ProductsAdapter(this, productArrayList, abc, new ProductsAdapter.OnProductStatusChanged() {
            @Override
            public void onStatusChanged(View v, Product product, final boolean status) {
                mDatabase.child("Products").child(product.getId()).child("isActive").setValue("" + status).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (status) {
                            CommonUtils.showToast("Product is now active");
                        } else {
                            CommonUtils.showToast("Product is now inactive");
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        CommonUtils.showToast("There was some error");

                    }
                });

            }

            @Override
            public void deleteProduct(final Product product) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListOfProducts.this);
                builder.setTitle("Alert");
                builder.setMessage("Do you want to delete this product? ");

                // add the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase.child("Products").child(product.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Deleted");
                            }
                        });

                    }
                });
                builder.setNegativeButton("Cancel", null);

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }

            @Override
            public void finishActivity() {
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
        getProductsFromDB();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ListOfProducts.this, AddProduct.class);
                startActivity(i);
            }
        });

    }

    private void getProductsFromDB() {
        mDatabase.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    productArrayList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Product product = snapshot.getValue(Product.class);
                        if (product != null) {
                            productArrayList.add(product);
                            Collections.sort(productArrayList, new Comparator<Product>() {
                                @Override
                                public int compare(Product listData, Product t1) {
                                    String ob1 = listData.getTitle();
                                    String ob2 = t1.getTitle();

                                    return ob1.compareTo(ob2);

                                }
                            });

                        }
                    }
                    adapter.updateList(productArrayList);
                    adapter.notifyDataSetChanged();
                } else {
                    productArrayList.clear();
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        final MenuItem mSearch = menu.findItem(R.id.action_search);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
//        mSearch.expandActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);

                return false;
            }
        });


        return true;
        // Get SearchView object.

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ListOfProducts.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {

            Intent i = new Intent(ListOfProducts.this, MainActivity.class);
            startActivity(i);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

}
