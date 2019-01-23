package com.clicknshop.goshopadmin.Activities.Categories;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.clicknshop.goshopadmin.Models.Product;
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

public class AddCategories extends AppCompatActivity {


    RecyclerView recycler;
    Button addCategory;
    EditText category, position;
    ArrayList<MainCategoryModel> itemList = new ArrayList<>();
    DatabaseReference mDatabase;
    CategoriesListAdapter adapter;
    public static Activity fa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);
        fa = this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Add Category");
        recycler = findViewById(R.id.recycler);
        category = findViewById(R.id.category);
        position = findViewById(R.id.position);
        addCategory = findViewById(R.id.addCategory);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (category.getText().length() == 0) {
                    category.setError("Enter Category");
                }
//                else if (position.getText().length() == 0) {
//                    position.setError("Enter Position");
//                }
//
                else {
                    sendCategoryToDB();
                }
            }
        });
        setUpRecyclerView();
        getDataFromDB();
    }

    private void getDataFromDB() {
        mDatabase.child("Categories").child("MainCategories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MainCategoryModel model = snapshot.getValue(MainCategoryModel.class);
                        if (model != null) {
                            itemList.add(model);
                            Collections.sort(itemList, new Comparator<MainCategoryModel>() {
                                @Override
                                public int compare(MainCategoryModel listData, MainCategoryModel t1) {
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
        adapter = new CategoriesListAdapter(this, itemList, 0, 0, new CategoriesListAdapter.ChooseOption() {
            @Override
            public void deleteCategory(String category) {
                deleteCategoryFromDB(category);
            }
        });
        recycler.setAdapter(adapter);

    }

    private void deleteCategoryFromDB(final String category) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCategories.this);
        builder.setTitle("Alert");
        builder.setMessage("Delete Category?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mDatabase.child("Categories").child("MainCategories").child(category).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Category Deleted");
                        adapter.notifyDataSetChanged();
                    }
                });
                mDatabase.child("Categories").child("SubCategories").child(category).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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
        mDatabase.child("Categories").child("MainCategories").child(category.getText().toString())
                .setValue(new MainCategoryModel(category.getText().toString(), Integer.parseInt(position.getText().toString())))
//                .setValue(new MainCategoryModel(category.getText().toString(), 1Integer.parseInt(position.getText().toString())))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        category.setText("");
                        position.setText("");
                        adapter.notifyDataSetChanged();
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
