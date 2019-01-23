package com.clicknshop.goshopadmin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicknshop.goshopadmin.Activities.ViewOrder;
import com.clicknshop.goshopadmin.Models.OrderModel;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by AliAh on 30/06/2018.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderModel> itemList;
    ChangeStatus changeStatus;


    public OrdersAdapter(Context context, ArrayList<OrderModel> itemList, ChangeStatus changeStatus) {
        this.context = context;
        this.itemList = itemList;
        this.changeStatus = changeStatus;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false);
        OrdersAdapter.ViewHolder viewHolder = new OrdersAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final OrderModel model = itemList.get(position);
        if (model != null) {

            if (model.getOrderStatus().equalsIgnoreCase("Pending")) {
                holder.checkbox.setVisibility(View.VISIBLE);
            } else {
                holder.checkbox.setVisibility(View.GONE);
            }
            if (model.getOrderStatus().equalsIgnoreCase("Pending")) {
                holder.cancel.setVisibility(View.VISIBLE);
            } else {
                holder.cancel.setVisibility(View.GONE);
            }

            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeStatus.markOrderAsCancelled(model);
                }
            });
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        changeStatus.markOrderAsUnderProcess(model, b);
                    } else {
                        changeStatus.markOrderAsUnderProcess(model, b);
                    }
                }
            });

            holder.orderDetails.setText("Order Id: " + model.getOrderId()
                    + "\n\nOrder Time: " + CommonUtils.getFormattedDate(model.getTime())
                    + "\n\nOrder Status: " + model.getOrderStatus()
                    + "\n\nOrder Items: " + model.getCountModelArrayList().size()
                    + "\n\nOrder Amount: TSh." + model.getTotalPrice()
            );
            holder.userDetails.setText("Name: " + model.getCustomer().getName()
                    + "\n\nAddress: " + model.getCustomer().getAddress()
                    + "\n\nPhone: " + model.getCustomer().getPhone()

            );
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, ViewOrder.class);
                    i.putExtra("orderId", model.getOrderId());
                    context.startActivity(i);
                }
            });
            holder.dial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getCustomer().getPhone()));
                    context.startActivity(i);
                }
            });


        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userDetails, orderDetails;
        ImageView dial, cancel;
        CheckBox checkbox;

        public ViewHolder(View itemView) {
            super(itemView);
            userDetails = itemView.findViewById(R.id.userDetails);
            orderDetails = itemView.findViewById(R.id.orderDetails);
            dial = itemView.findViewById(R.id.dial);
            cancel = itemView.findViewById(R.id.cancel);
            checkbox = itemView.findViewById(R.id.checkbox);
        }
    }

    public interface ChangeStatus {
        public void markOrderAsUnderProcess(OrderModel order, boolean b);

        public void markOrderAsCancelled(OrderModel order);
    }
}
