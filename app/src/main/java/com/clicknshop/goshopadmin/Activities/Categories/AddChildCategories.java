package com.clicknshop.goshopadmin.Activities.Categories;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.clicknshop.goshopadmin.Activities.AddProduct;
import com.clicknshop.goshopadmin.Activities.EditProduct;
import com.clicknshop.goshopadmin.Models.ChildCategoryModel;
import com.clicknshop.goshopadmin.R;
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

public class AddChildCategories extends AppCompatActivity {


    RecyclerView recycler;
    Button addCategory;
    EditText category, position;
    ArrayList<ChildCategoryModel> itemList = new ArrayList<>();
    DatabaseReference mDatabase;
    ChildCategoriesListAdapter adapter;
    String subCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child_categories);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        subCategory = getIntent().getStringExtra("subCategory");
        this.setTitle("Subcategory: " + subCategory);
        recycler = findViewById(R.id.recycler);
        category = findViewById(R.id.category);
        position = findViewById(R.id.position);
        addCategory = findViewById(R.id.addCategory);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (category.getText().length() == 0) {
                    category.setError("Enter Value");
                } else if (position.getText().length() == 0) {
                    position.setError("Enter Value");
                } else {
                    sendCategoryToDB();
                }
            }
        });
        setUpRecyclerView();
        getDataFromDB();
    }

    private void getDataFromDB() {
        mDatabase.child("Categories").child("ChildCategory").child(subCategory).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChildCategoryModel model = snapshot.getValue(ChildCategoryModel.class);
                        if (model != null) {
                            itemList.add(model);
                            Collections.sort(itemList, new Comparator<ChildCategoryModel>() {
                                @Override
                                public int compare(ChildCategoryModel listData, ChildCategoryModel t1) {
                                    Integer ob1 = listData.getPosition();
                                    Integer ob2 = t1.getPosition();

                                    return ob2.compareTo(ob1);

                                }
                            });
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    itemList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpRecyclerView() {
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new ChildCategoriesListAdapter(this, itemList, 0, 0, new ChildCategoriesListAdapter.ChooseOption() {
            @Override
            public void deleteCategory(String category) {
                deleteCategoryFromDB(category);
            }

            @Override
            public void finishActivity(String category) {
                AddProduct.category = category;
                EditProduct.category=category;
                AddCategories.fa.finish();
                AddSubCategory.fa.finish();
                finish();
            }
        });
        recycler.setAdapter(adapter);

    }

    private void deleteCategoryFromDB(final String category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddChildCategories.this);
        builder.setTitle("Alert");
        builder.setMessage("Delete Category?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mDatabase.child("Categories").child("ChildCategory").child(subCategory).child(category).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                CommonUtils.showToast("Category Deleted");
                                adapter.notifyDataSetChanged();
                            }
                        });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void sendCategoryToDB() {
        mDatabase.child("Categories").child("ChildCategory").child(subCategory).child(category.getText().toString())
                .setValue(new ChildCategoryModel(
                        category.getText().toString(),
                        Integer.parseInt(position.getText().toString())

                ))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        category.setText("");
                        position.setText("");
                        adapter.notifyDataSetChanged();
                        CommonUtils.showToast("Category Added");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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
