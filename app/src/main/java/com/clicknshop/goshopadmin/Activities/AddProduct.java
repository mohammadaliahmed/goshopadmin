package com.clicknshop.goshopadmin.Activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clicknshop.goshopadmin.Activities.Categories.AddCategories;
import com.clicknshop.goshopadmin.Activities.ImageCrop.PickerBuilder;
import com.clicknshop.goshopadmin.Models.Product;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;
import com.clicknshop.goshopadmin.Utils.CompressImage;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddProduct extends AppCompatActivity {
    private static final int PIC_CROP = 2;
    TextView categoryChoosen, brandChosen;
    StorageReference mStorageRef;
    DatabaseReference mDatabase;
    Button upload;
    ImageView pick, camera;
    private static final int REQUEST_CODE_CHOOSE = 23;
    private static final int REQUEST_CODE_CAMERA = 25;
    List<Uri> mSelected;
    ImageView img;
    Bundle extras;
    String imagePath;
    EditText e_title, e_subtitle, e_price;
    String productId;
    ProgressBar progressBar;
    private File cameraPhotoFile;


    final int CAMERA_CAPTURE = 1;
    //captured picture uri
    private Uri picUri;

    @Override
    protected void onResume() {
        super.onResume();
        if (category == null) {

        } else {
            categoryChoosen.setText("Category: " + category);

        }
    }

    public static String category, brand;
    EditText oldPrice;
    EditText costPrice;

    final int CROP_PIC = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Add Product");
        getPermissions();
        categoryChoosen = findViewById(R.id.categoryChoosen);


        pick = findViewById(R.id.pick);
        upload = findViewById(R.id.upload);
        img = findViewById(R.id.image);
        e_title = findViewById(R.id.title);
        e_subtitle = findViewById(R.id.subtitle);
        e_price = findViewById(R.id.price);
        progressBar = findViewById(R.id.prgress);
        brandChosen = findViewById(R.id.brandChosen);
        oldPrice = findViewById(R.id.oldPrice);
        costPrice = findViewById(R.id.costPrice);
        camera = findViewById(R.id.camera);

        categoryChoosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddProduct.this, AddCategories.class);
                startActivityForResult(i, 1);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelected != null) {
                    mSelected.clear();
                }
                new PickerBuilder(AddProduct.this, PickerBuilder.SELECT_FROM_CAMERA)
                        .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                            @Override
                            public void onImageReceived(Uri imageUri) {
//                                Toast.makeText(AddProduct.this,"Got image - " + imageUri,Toast.LENGTH_LONG).show();
                                mSelected = new ArrayList<>();
                                mSelected.add(imageUri);
                                Glide.with(AddProduct.this).load(mSelected.get(0)).into(img);
                                CompressImage compressImage = new CompressImage(AddProduct.this);
                                imagePath = compressImage.compressImage("" + mSelected.get(0));
                            }
                        })
                        .start();
            }
        });

        brandChosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(AddProduct.this, ChooseBrand.class);
                startActivityForResult(i, 2);
            }
        });


        pick.setOnClickListener(new View.OnClickListener() {
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
                new PickerBuilder(AddProduct.this, PickerBuilder.SELECT_FROM_GALLERY)
                        .setOnImageReceivedListener(new PickerBuilder.onImageReceivedListener() {
                            @Override
                            public void onImageReceived(Uri imageUri) {
//                                Toast.makeText(AddProduct.this,"Got image - " + imageUri,Toast.LENGTH_LONG).show();
                                mSelected = new ArrayList<>();
                                mSelected.add(imageUri);
                                Glide.with(AddProduct.this).load(mSelected.get(0)).into(img);
                                CompressImage compressImage = new CompressImage(AddProduct.this);
                                imagePath = compressImage.compressImage("" + mSelected.get(0));
                            }
                        })
                        .start();
            }

        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (category == null) {
                    CommonUtils.showToast("Choose Category");

                } else if (e_title.getText().length() == 0) {
                    e_title.setError("Enter title");
                } else if (e_subtitle.getText().length() == 0) {
                    e_subtitle.setError("Enter subtitle");
                } else if (e_price.getText().length() == 0) {
                    e_price.setError("Enter price");
                } else if (costPrice.getText().length() == 0) {
                    costPrice.setError("Enter cost price");
                } else if (imagePath == null) {
                    CommonUtils.showToast("Please choose picture");
                } else {

                    progressBar.setVisibility(View.VISIBLE);
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Products");
                    productId = mDatabase.push().getKey();
                    mDatabase.child(productId).setValue(new Product(productId,
                            e_title.getText().toString(),
                            e_subtitle.getText().toString(),
                            category,
                            Integer.parseInt(e_price.getText().toString()),
                            "true",
                            "abc",
                            "",
                            System.currentTimeMillis(),
                            brand,
                            Integer.parseInt(oldPrice.getText().toString().equalsIgnoreCase("") ? "0" : oldPrice.getText().toString()),
                            Integer.parseInt(costPrice.getText().toString())

                    )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            putPictures(imagePath, productId);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
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

                        mDatabase.child("" + key).child("thumbnailUrl").setValue("" + downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressBar.setVisibility(View.GONE);
                                Intent i = new Intent(AddProduct.this, ProductUploaded.class);
                                startActivity(i);
                                finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null) {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                mSelected = Matisse.obtainResult(data);
                Glide.with(this).load(mSelected.get(0)).into(img);
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
