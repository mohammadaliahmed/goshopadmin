package com.clicknshop.goshopadmin.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicknshop.goshopadmin.Adapters.OrderedProductsAdapter;
import com.clicknshop.goshopadmin.Models.InvoiceModel;
import com.clicknshop.goshopadmin.Models.OrderModel;
import com.clicknshop.goshopadmin.Models.ProductCountModel;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;
import com.clicknshop.goshopadmin.Utils.NotificationAsync;
import com.clicknshop.goshopadmin.Utils.NotificationObserver;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewOrder extends AppCompatActivity implements NotificationObserver {
    TextView orderId, orderTime, quantity, price, username, phone, address, city, info, day, timeChosen;
    String orderIdFromIntent;
    DatabaseReference mDatabase;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    OrderedProductsAdapter adapter;
    ArrayList<ProductCountModel> list = new ArrayList<>();
    Button orderCompleted, orderShipped;
    OrderModel model;
    long billNumber = 1;

    String s_orderId, s_orderTime, s_quantity, s_price, s_username, s_phone, s_address, s_city;
    String userFcmKey;
    FloatingActionButton invoice;
    ImageView viewOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        this.setTitle("Order View");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        orderIdFromIntent = intent.getStringExtra("orderId");
        orderId = findViewById(R.id.order_id);
        orderTime = findViewById(R.id.order_time);
        quantity = findViewById(R.id.order_quantity);
        price = findViewById(R.id.order_price);
        invoice = findViewById(R.id.invoice);
        viewOnMap = findViewById(R.id.viewOnMap);

        info = findViewById(R.id.info);
        username = findViewById(R.id.ship_username);
        phone = findViewById(R.id.ship_phone);
        address = findViewById(R.id.ship_address);
        city = findViewById(R.id.ship_city);
        timeChosen = findViewById(R.id.timeChosen);
        day = findViewById(R.id.day);


        orderCompleted = findViewById(R.id.completed);
        orderShipped = findViewById(R.id.shipped);

        getInvoiceCountFromDB();

        viewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String uri = "http://maps.google.com/maps?saddr=" + 31.5123929  + "," + 74.2144306 + "&daddr=" + model.getLat() + "," +model.getLon() ;
                String uri = "https://maps.google.com/?daddr=" + model.getLat() + "," + model.getLon();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
        orderShipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                markOrderAsShipped();
            }
        });

        orderCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markOrderAsComplete();
            }
        });

        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createInvoice();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        mDatabase.child("Orders").child(orderIdFromIntent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    model = dataSnapshot.getValue(OrderModel.class);
                    if (model != null) {
                        orderId.setText("" + model.getOrderId());
                        orderTime.setText("" + CommonUtils.getFormattedDate(model.getTime()));
                        quantity.setText("" + model.getCountModelArrayList().size());
                        price.setText("TSh " + model.getTotalPrice());
                        username.setText("" + model.getCustomer().getName());
                        phone.setText("" + model.getCustomer().getPhone());
                        address.setText(model.getCustomer().getAddress());
                        city.setText(model.getCustomer().getCity());
                        info.setText("Instructions: " + model.getInstructions());
                        day.setText("Day: " + model.getDate());
                        timeChosen.setText("Time : " + model.getChosenTime());
                        list = model.getCountModelArrayList();
                        adapter = new OrderedProductsAdapter(ViewOrder.this, list);
                        recyclerView.setAdapter(adapter);


                        if (model.getOrderStatus().equalsIgnoreCase("Pending")) {
                            orderCompleted.setVisibility(View.GONE);
                            orderShipped.setVisibility(View.VISIBLE);

                        } else if (model.getOrderStatus().equalsIgnoreCase("under process")) {
                            invoice.setVisibility(View.VISIBLE);
                            orderCompleted.setVisibility(View.GONE);
                            orderShipped.setVisibility(View.VISIBLE);
                        } else if (model.getOrderStatus().equalsIgnoreCase("Shipped")) {
                            orderShipped.setVisibility(View.GONE);
                            orderCompleted.setVisibility(View.VISIBLE);
                        } else {
                            orderShipped.setVisibility(View.GONE);
                            orderCompleted.setVisibility(View.GONE);
                        }
//                        Toast.makeText(ViewOrder.this, ""+list, Toast.LENGTH_SHORT).show();
//                        adapter.notifyDataSetChanged();

                        s_orderId = model.getOrderId();
                        s_quantity = "" + model.getCountModelArrayList().size();
                        s_price = "" + model.getTotalPrice();
                        s_username = model.getCustomer().getUsername();

                        userFcmKey = model.getCustomer().getFcmKey();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getInvoiceCountFromDB() {
        mDatabase.child("Invoices").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    billNumber = dataSnapshot.getChildrenCount() + 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void createInvoice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewOrder.this);
        builder.setTitle("Alert");
        builder.setMessage("Create Bill?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Invoices").child("" + billNumber).setValue(new InvoiceModel("" + billNumber, model, System.currentTimeMillis())).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateInvoiceInOrder();
                        Intent i = new Intent(ViewOrder.this, ViewInvoice.class);
                        i.putExtra("invoiceId", "" + billNumber);
                        startActivity(i);
                    }
                });

            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void updateInvoiceInOrder() {
        mDatabase.child("Orders").child(orderIdFromIntent).child("billNumber").setValue(billNumber).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }


    private void markOrderAsComplete() {
        showAlertDialogButtonClicked("Completed");
    }

    private void markOrderAsShipped() {
        showAlertDialogButtonClicked("Shipped");
    }

    public void showAlertDialogButtonClicked(final String message) {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewOrder.this);
        builder.setTitle("Alert");
        builder.setMessage("Do you want to mark this order as " + message + "?");

        // add the buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatabase.child("Orders").child(orderIdFromIntent).child("orderStatus").setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CommonUtils.showToast("Order marked as " + message);
                        NotificationAsync notificationAsync = new NotificationAsync(ViewOrder.this);
                        String notification_title = "You order has been " + message;
                        String notification_message = "Click to view";
                        notificationAsync.execute("ali", userFcmKey, notification_title, notification_message, "Order", "abc");
                        Intent i = new Intent(ViewOrder.this, Orders.class);
                        startActivity(i);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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
    public void onSuccess(String chatId) {
        CommonUtils.showToast("Notification sent to user");
    }

    @Override
    public void onFailure() {

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
