package com.clicknshop.goshopadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clicknshop.goshopadmin.Activities.ViewInvoice;
import com.clicknshop.goshopadmin.Models.InvoiceModel;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;

import java.util.ArrayList;

public class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.ViewHolder> {
    Context context;
    ArrayList<InvoiceModel> itemList;

    public BillsAdapter(Context context, ArrayList<InvoiceModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public BillsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bill_item_layout, parent, false);
        BillsAdapter.ViewHolder viewHolder = new BillsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BillsAdapter.ViewHolder holder, int position) {
        final InvoiceModel model = itemList.get(position);
        holder.orderDetails.setText("Bill Number: " + model.getInvoiceId()
                + "\n\nOrder Number: " + model.getOrder().getOrderId()
                + "\n\nTotal Price: TSh " + model.getOrder().getTotalPrice()
                + "\n\nBill Time: " + CommonUtils.getFormattedDate(model.getTime())
        );
        holder.userDetails.setText("Name: " + model.getOrder().getCustomer().getName()
                + "\n\nAddress: " + model.getOrder().getCustomer().getAddress()
                + "\n\nPhone: " + model.getOrder().getCustomer().getPhone()

        );
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, ViewInvoice.class);
                i.putExtra("invoiceId", model.getInvoiceId());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderDetails, userDetails;

        public ViewHolder(View itemView) {
            super(itemView);
            userDetails = itemView.findViewById(R.id.userDetails);
            orderDetails = itemView.findViewById(R.id.orderDetails);
        }
    }
}
