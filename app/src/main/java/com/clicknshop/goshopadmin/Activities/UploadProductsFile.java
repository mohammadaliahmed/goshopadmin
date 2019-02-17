package com.clicknshop.goshopadmin.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clicknshop.goshopadmin.Adapters.ProductFileListAdapter;
import com.clicknshop.goshopadmin.Models.Product;
import com.clicknshop.goshopadmin.Models.ProductUploadModel;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UploadProductsFile extends AppCompatActivity {
    DatabaseReference mDatabase;
    RelativeLayout pickFile;
    private static final int REQUEST_CODE_FILE = 25;
    List<String[]> recipientList = new ArrayList<>();
    Button upload;

    TextView fileChosen;
    RecyclerView recycler;
    ProductFileListAdapter adapter;
    private ArrayList<ProductUploadModel> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_products_file);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Upload products file");
        getPermissions();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        pickFile = findViewById(R.id.pickFile);
        fileChosen = findViewById(R.id.fileChosen);
        recycler = findViewById(R.id.recycler);
        upload = findViewById(R.id.upload);
        pickFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile(REQUEST_CODE_FILE);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < recipientList.size(); i++) {
                    uploadToDB(i);
                }
                CommonUtils.showToast("Uploading");
                Intent i = new Intent(UploadProductsFile.this, ListOfProducts.class);
                startActivity(i);

            }
        });
    }

    private void uploadToDB(int i) {
        String key = mDatabase.push().getKey();
        mDatabase.child("Products").child(key).setValue(new Product(
                key,
                recipientList.get(i)[0],
                recipientList.get(i)[1],
                recipientList.get(i)[3],
                Integer.parseInt(recipientList.get(i)[2]),
                "true",
                "abc",
                "",
                System.currentTimeMillis(),
                "",
                0,
                0
        ));
    }

    private void openFile(Integer CODE) {
        recipientList.clear();
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        startActivityForResult(i, CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FILE && data != null) {
            Uri uri = data.getData();
            String abc = data.getData().getPath().substring(data.getData().getPath().lastIndexOf("/") + 1);
            fileChosen.setText("" + abc);

            readCSV(uri);
        }
    }

    private void readCSV(Uri uri) {
        InputStream file = null;
        try {
            file = getContentResolver().openInputStream(uri);
            CSVReader csvReader = new CSVReader(new InputStreamReader(file));
            recipientList = csvReader.readAll();
//            adapter.notifyDataSetChanged();
            recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            adapter = new ProductFileListAdapter(this, recipientList);
            recipientList.remove(0);
            recycler.setAdapter(adapter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

}
