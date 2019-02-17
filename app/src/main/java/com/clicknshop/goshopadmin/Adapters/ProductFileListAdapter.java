package com.clicknshop.goshopadmin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clicknshop.goshopadmin.Activities.ProductUploaded;
import com.clicknshop.goshopadmin.Models.ProductUploadModel;
import com.clicknshop.goshopadmin.R;
import com.clicknshop.goshopadmin.Utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductFileListAdapter extends RecyclerView.Adapter<ProductFileListAdapter.ViewHolder> {
    Context context;
    List<String[]> itemList;

    public ProductFileListAdapter(Context context, List<String[]> itemList) {

        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);
        ProductFileListAdapter.ViewHolder viewHolder = new ProductFileListAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            String[] line = itemList.get(position);
            holder.serial.setText("" + (position + 1));
            holder.title.setText(line[0]);
            holder.subTitle.setText(line[1]);
            holder.price.setText(line[2]);
            holder.category.setText(line[3]);
        } catch (ArrayIndexOutOfBoundsException e) {
//            CommonUtils.showToast("This is not a valid file");
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView serial, title, subTitle, price, category;

        public ViewHolder(View itemView) {
            super(itemView);
            serial = itemView.findViewById(R.id.serial);
            title = itemView.findViewById(R.id.title);
            subTitle = itemView.findViewById(R.id.subTitle);
            price = itemView.findViewById(R.id.price);
            category = itemView.findViewById(R.id.category);
        }
    }
}
