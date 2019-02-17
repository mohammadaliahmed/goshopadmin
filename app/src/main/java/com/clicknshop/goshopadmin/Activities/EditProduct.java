package com.clicknshop.goshopadmin.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicknshop.goshopadmin.Activities.Categories.AddCategories;
import com.clicknshop.goshopadmin.Activities.ImageCrop.PickerBuilder;
import com.clicknshop.goshopadmin.Models.Product;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;
import com.bumptech.glide.Glide;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditProduct extends AppCompatActivity {
    String productId;
    DatabaseReference mDatabaseReference;
    ImageView image;
    Button update;
    EditText e_title, e_price, e_subtitle, e_costPrice, e_oldPrice;
    TextView categoryChoosen, brandChosen;
    public static String category, brand;
    private Bundle extras;
    StorageReference mStorageRef;
    List<Uri> mSelected = new ArrayList<>();
    private static final int REQUEST_CODE_CHOOSE = 23;
    String imagePath;


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
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Products");

        e_title = findViewById(R.id.title);
        e_price = findViewById(R.id.price);
        e_costPrice = findViewById(R.id.costPrice);
        e_oldPrice = findViewById(R.id.oldPrice);
        e_subtitle = findViewById(R.id.subtitle);
        update = findViewById(R.id.update);
        brandChosen = findViewById(R.id.brandChosen);
        categoryChoosen = findViewById(R.id.categoryChoosen);

        brandChosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(EditProduct.this, ChooseBrand.class);
                startActivityForResult(i, 2);
            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Matisse.from(AddProduct.this)
//                        .choose(MimeType.allOf())
//                        .countable(true)
//                        .maxSelectable(1)
//                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
//                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
//                        .thumbnailScale(0.85f)
//                        .imageEngine(new GlideEngine())
//                        .forResult(REQUEST_CODE_CHOOSE);
                if (mSelected != null) {
                    mSelected.clear();
                }
                new PickerBuilder(EditProduct.this, PickerBuilder.SELECT_FROM_GALLERY)
                        .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                            @Override
                            public void onImageReceived(Uri imageUri) {
//                                Toast.makeText(AddProduct.this,"Got image - " + imageUri,Toast.LENGTH_LONG).show();
                                mSelected = new ArrayList<>();
                                mSelected.add(imageUri);
                                Glide.with(EditProduct.this).load(mSelected.get(0)).into(image);
                                CompressImage compressImage = new CompressImage(EditProduct.this);
                                imagePath = compressImage.compressImage("" + mSelected.get(0));
                            }
                        })
                        .start();
            }

        });



        mDatabaseReference.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        EditProduct.this.setTitle("Edit: " + product.getTitle());

                        e_title.setText(product.getTitle());
                        e_price.setText("" + product.getPrice());
                        e_subtitle.setText(product.getSubtitle());
                        e_costPrice.setText("" + product.getCostPrice());
                        e_oldPrice.setText("" + product.getOldPrice());
                        Glide.with(EditProduct.this).load(product.getThumbnailUrl()).placeholder(R.drawable.ic_pick_image).into(image);
                        categoryChoosen.setText("Category: " + product.getCategory());
                        brandChosen.setText("Brand: " + product.getBrand());
                        category = product.getCategory();
                        brand = product.getBrand();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        categoryChoosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditProduct.this, AddCategories.class);
                startActivityForResult(i, 1);
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (e_title.getText().length() != 0 && e_subtitle.getText().length() != 0 && e_price.getText().length() != 0) {
                    mDatabaseReference.child(productId).child("price").setValue(Integer.parseInt(e_price.getText().toString()));
                    mDatabaseReference.child(productId).child("title").setValue(e_title.getText().toString());
                    mDatabaseReference.child(productId).child("brand").setValue(brand);
                    mDatabaseReference.child(productId).child("category").setValue(category);
                    mDatabaseReference.child(productId).child("costPrice").setValue(Integer.parseInt(e_costPrice.getText().toString()));
                    mDatabaseReference.child(productId).child("oldPrice")
                            .setValue(Integer.parseInt(e_oldPrice.getText().toString().equalsIgnoreCase("") ? "0" : e_oldPrice.getText().toString()));
                    mDatabaseReference.child(productId).child("title").setValue(e_title.getText().toString());
                    if (mSelected.size() == 0) {

                    } else {
//                        mDatabaseReference.child(productId).child("thumbnailUrl").set
                        putPictures(imagePath, productId);
                    }


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

                        mDatabaseReference.child("" + key).child("thumbnailUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                Intent i = new Intent(EditProduct.this, ProductUploaded.class);
//                                startActivity(i);
//                                finish();
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

    @Override
    protected void onResume() {
        super.onResume();
        if (category == null) {

        } else {
            categoryChoosen.setText("Category: " + category);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                mSelected = Matisse.obtainResult(data);
                Glide.with(this).load(mSelected.get(0)).into(image);
                CompressImage compressImage = new CompressImage(this);
                imagePath = compressImage.compressImage("" + mSelected.get(0));
            }
            if (requestCode == 1) {
                extras = data.getExtras();
                if (extras.getString("category") != null) {
                    categoryChoosen.setText("Category: " + extras.getString("category"));
                    category = extras.getString("category");

                }
            }
            if (requestCode == 2) {
                extras = data.getExtras();
                if (extras.getString("brand") != null) {
                    brandChosen.setText("Brand: " + extras.getString("brand"));
                    brand = extras.getString("brand");

                }
            }


            super.onActivityResult(requestCode, resultCode, data);
        }
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
