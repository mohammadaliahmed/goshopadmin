package com.clicknshop.goshopadmin.Activities.Categories;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;
import com.clicknshop.goshopadmin.Utils.CompressImage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddSubCategory extends AppCompatActivity {


    RecyclerView recycler;
    Button addCategory;
    EditText category, position;
    ArrayList<SubCategoryModel> itemList = new ArrayList<>();
    DatabaseReference mDatabase;
    SubCategoriesListAdapter adapter;
    String mainCategory;
    ImageView categoryImage;
    String imagePath;
    List<Uri> mSelected;

    StorageReference mStorageRef;

    private static final int REQUEST_CODE_CHOOSE = 23;

    public static Activity fa;
    int mainPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_categories);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getPermissions();
        fa = this;
        this.setTitle("Add Subcategory");
        recycler = findViewById(R.id.recycler);
        category = findViewById(R.id.category);
        addCategory = findViewById(R.id.addCategory);
        categoryImage = findViewById(R.id.categoryImage);
        position = findViewById(R.id.position);

        mainCategory = getIntent().getStringExtra("mainCategory");
        mainPos = getIntent().getIntExtra("position", 1);
        this.setTitle("Main: " + mainCategory);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (category.getText().length() == 0) {
                    category.setError("Enter category");
                } else if (position.getText().length() == 0) {
                    position.setError("Enter position");
                } else if (imagePath == null) {
                    CommonUtils.showToast("Please choose picture");
                } else {
                    sendCategoryToDB();
                }
            }
        });
        categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Matisse.from(AddSubCategory.this)
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(1)
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });
        setUpRecyclerView();
        getDataFromDB();
    }


    public void putPictures(String path, final String key) {
        String imgName = Long.toHexString(Double.doubleToLongBits(Math.random()));

        Uri file = Uri.fromFile(new File(path));

        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference riversRef = mStorageRef.child("Photos").child(imgName);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    @SuppressWarnings("VisibleForTests")
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content

                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        mDatabase.child("Categories").child("SubCategories").child(mainCategory).child("categories").child("" + key)
                                .child("picUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                adapter.notifyDataSetChanged();
                                mSelected.clear();
                                imagePath = null;
                                try {
                                    Glide.with(AddSubCategory.this).load(R.drawable.ic_pick_image).into(categoryImage);
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                CommonUtils.showToast("There was some error uploading pic");
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        CommonUtils.showToast("There was some error uploading pic");

                    }
                });


    }


    private void getDataFromDB() {
        mDatabase.child("Categories").child("SubCategories").child(mainCategory).child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    itemList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SubCategoryModel model = snapshot.getValue(SubCategoryModel.class);
                        if (model != null) {
                            itemList.add(model);
                            Collections.sort(itemList, new Comparator<SubCategoryModel>() {
                                @Override
                                public int compare(SubCategoryModel listData, SubCategoryModel t1) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                mSelected = Matisse.obtainResult(data);
                Glide.with(this).load(mSelected.get(0)).into(categoryImage);
                CompressImage compressImage = new CompressImage(this);
                imagePath = compressImage.compressImage("" + mSelected.get(0));
            }


            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setUpRecyclerView() {
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new SubCategoriesListAdapter(this, itemList, 0, 0, new SubCategoriesListAdapter.ChooseOption() {
            @Override
            public void deleteCategory(String category, String subCategory) {
                deleteCategoryFromDB(category, subCategory);
            }
        });
        recycler.setAdapter(adapter);

    }

    private void deleteCategoryFromDB(final String category, final String subCategory) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddSubCategory.this);
        builder.setTitle("Alert");
        builder.setMessage("Delete Category?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mDatabase.child("Categories").child("SubCategories").child(mainCategory).child("categories").child(subCategory).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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

        SubCategoryModel m = new SubCategoryModel(
                category.getText().toString()
                , mainCategory,
                "",
                Integer.parseInt(position.getText().toString()));
        itemList.add(m);


        mDatabase.child("Categories").child("SubCategories").child(mainCategory).child("position").setValue(mainPos);
        mDatabase.child("Categories").child("SubCategories").child(mainCategory).child("categories").child(category.getText().toString())
                .setValue(m)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        String cat = category.getText().toString();
                        category.setText("");
                        position.setText("");
                        adapter.notifyDataSetChanged();
                        putPictures(imagePath, cat);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    private void getPermissions() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
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
