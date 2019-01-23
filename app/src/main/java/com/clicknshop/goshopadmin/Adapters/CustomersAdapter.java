package com.clicknshop.goshopadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicknshop.goshopadmin.Activities.ViewCustomer;
import com.clicknshop.goshopadmin.Models.Customer;
import com.clicknshop.goshopadmin.R;

import java.util.ArrayList;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.ViewHolder> {
    Context context;
    ArrayList<Customer> itemList;

    public CustomersAdapter(Context context, ArrayList<Customer> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_item_layout, parent, false);
        CustomersAdapter.ViewHolder viewHolder = new CustomersAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Customer model = itemList.get(position);
        holder.username.setText(model.getName());
        holder.subtitle.setText("");
        holder.dial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getPhone()));
                context.startActivity(i);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ViewCustomer.class);
                i.putExtra("customerId", model.getId());

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, subtitle;
        ImageView dial;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            subtitle = itemView.findViewById(R.id.subtitle);
            dial = itemView.findViewById(R.id.dial);
        }
    }
}
