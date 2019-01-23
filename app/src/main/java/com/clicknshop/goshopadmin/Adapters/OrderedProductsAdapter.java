package com.clicknshop.goshopadmin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicknshop.goshopadmin.Models.ProductCountModel;
import com.clicknshop.goshopadmin.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by AliAh on 30/06/2018.
 */

public class OrderedProductsAdapter extends RecyclerView.Adapter<OrderedProductsAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductCountModel> productList;

    public OrderedProductsAdapter(Context context, ArrayList<ProductCountModel> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ordered_product_layout, parent, false);
        OrderedProductsAdapter.ViewHolder viewHolder = new OrderedProductsAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ProductCountModel model = productList.get(position);

        holder.title.setText(model.getProduct().getTitle());
        holder.price.setText("TSh. " + model.getProduct().getPrice());
        holder.subtitle.setText(model.getProduct().getSubtitle());
        holder.count.setText("Item Qty: "+model.getQuantity());
        holder.serial.setText(""+(position+1)+") ");

        Glide.with(context).load(model.getProduct().getThumbnailUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle, price, count,serial;
        ImageView image, increase, decrease;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.image);
            count = itemView.findViewById(R.id.quantity);
            serial = itemView.findViewById(R.id.serial);


        }
    }
}
