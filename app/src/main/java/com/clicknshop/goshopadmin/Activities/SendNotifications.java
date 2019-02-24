package com.clicknshop.goshopadmin.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.clicknshop.goshopadmin.Models.Customer;
import com.clicknshop.goshopadmin.Models.CustomerNotificationModel;
import com.clicknshop.goshopadmin.Models.Product;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;
import com.clicknshop.goshopadmin.Utils.NotificationAsync;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;


public class SendNotifications extends AppCompatActivity {
    EditText notificationMessage, notificationTitle;
    EditText notificationMessage1, notificationTitle1;
    EditText notificationMessage2, notificationTitle2;
    Button send;
    Button sendBrandNotification;
    Button sendProductNotification;
    TextView productChosen;
    DatabaseReference mDatabase;
    ArrayList<String> arrayList = new ArrayList<>();
    List<String> brandNames = new ArrayList<>();
    Spinner spinner;
    String brandChosen;
    public static Product product;
    Button sent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notifications);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Send Notification");
        notificationMessage = findViewById(R.id.notificationMessage);
        notificationTitle = findViewById(R.id.notificationTitle);
        productChosen = findViewById(R.id.productChosen);
        send = findViewById(R.id.send);
        notificationMessage2 = findViewById(R.id.notificationMessage2);
        notificationTitle2 = findViewById(R.id.notificationTitle2);
        sendProductNotification = findViewById(R.id.sendProductNotification);
        send = findViewById(R.id.send);
        spinner = findViewById(R.id.spinner);
        notificationTitle1 = findViewById(R.id.notificationTitle1);
        spinner = findViewById(R.id.spinner);
        notificationMessage1 = findViewById(R.id.notificationMessage1);
        sendBrandNotification = findViewById(R.id.sendBrandNotification);
        sent = findViewById(R.id.sent);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child("customers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Customer customer = snapshot.getValue(Customer.class);
                        if (customer != null) {
                            arrayList.add(customer.getFcmKey());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SendNotifications.this,NotificationHistory.class);
                startActivity(i);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificationTitle.getText().length() == 0) {
                    notificationTitle.setError("Enter Title");
                } else if (notificationMessage.getText().length() == 0) {
                    notificationMessage.setError("Enter Message");
                } else if (arrayList.size() == 0) {
                    CommonUtils.showToast("No Users");

                } else {

                    for (String keys : arrayList) {
                        sendNotification(keys);
                    }
                    updateNotificationInDB(notificationTitle.getText().toString(),
                            notificationMessage.getText().toString(), "marketing", "");
                    notificationTitle.setText("");
                    notificationMessage.setText("");
                    CommonUtils.showToast("Sent notification to all");
//                    finish();
                }
            }
        });
        sendBrandNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificationTitle1.getText().length() == 0) {
                    notificationTitle1.setError("Enter Title");
                } else if (notificationMessage1.getText().length() == 0) {
                    notificationMessage1.setError("Enter Message");
                } else if (brandChosen == null) {
                    CommonUtils.showToast("Please choose brand");
                } else if (arrayList.size() == 0) {
                    CommonUtils.showToast("No Users");

                } else {

                    for (String keys : arrayList) {
                        sendBrandNotifications(keys);
                    }

                    updateNotificationInDB(notificationTitle1.getText().toString(),
                            notificationMessage1.getText().toString(), "brand", brandChosen);
                    CommonUtils.showToast("Sent notification to all");
                    notificationTitle1.setText("");
                    notificationMessage1.setText("");
//                    finish();
                }
            }
        });
        productChosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SendNotifications.this, ListOfProducts.class);
                i.putExtra("from", "notificationScreen");
                startActivity(i);
            }
        });
        sendProductNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notificationTitle2.getText().length() == 0) {
                    notificationTitle2.setError("Enter Title");
                } else if (notificationMessage2.getText().length() == 0) {
                    notificationMessage2.setError("Enter Message");
                } else if (product == null) {
                    CommonUtils.showToast("Please choose product");
                } else if (arrayList.size() == 0) {
                    CommonUtils.showToast("No Users");

                } else {

                    for (String keys : arrayList) {
                        sendProductNotifications(keys);
                    }

                    updateNotificationInDB(notificationTitle2.getText().toString(),
                            notificationMessage2.getText().toString(), "product", product.getId());
                    CommonUtils.showToast("Sent notification to all");
                    notificationTitle2.setText("");
                    notificationMessage2.setText("");
//                    finish();
                }
            }
        });
        getDataFromDB();

    }

    private void updateNotificationInDB(String title, String message, String type, String id) {
        String key = mDatabase.push().getKey();
        mDatabase.child("CustomerNotifications").child(key).setValue(new CustomerNotificationModel(
                key, title, message, type, id, System.currentTimeMillis()
        )).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (product != null) {
            productChosen.setText("Product: " + product.getTitle());
        }
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
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SendNotifications.this,
                            android.R.layout.simple_spinner_item, brandNames);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            brandChosen = brandNames.get(i);

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    brandNames.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String keys) {
        NotificationAsync notificationAsync = new NotificationAsync(SendNotifications.this);
        String notification_title = notificationTitle.getText().toString();
        String notification_message = notificationMessage.getText().toString();
        notificationAsync.execute("ali", keys, notification_title, notification_message, "marketing", "abc");
    }

    private void sendBrandNotifications(String keys) {
        NotificationAsync notificationAsync = new NotificationAsync(SendNotifications.this);
        String notification_title = notificationTitle1.getText().toString();
        String notification_message = notificationMessage1.getText().toString();
        notificationAsync.execute("ali", keys, notification_title, notification_message, "brand", brandChosen);
    }

    private void sendProductNotifications(String keys) {
        NotificationAsync notificationAsync = new NotificationAsync(SendNotifications.this);
        String notification_title = notificationTitle2.getText().toString();
        String notification_message = notificationMessage2.getText().toString();
        notificationAsync.execute("ali", keys, notification_title, notification_message, "product", product.getId());
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
