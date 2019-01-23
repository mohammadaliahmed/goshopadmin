package com.clicknshop.goshopadmin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clicknshop.goshopadmin.R;

import java.util.ArrayList;

public class BrandsListAdapter extends RecyclerView.Adapter<BrandsListAdapter.ViewHolder> {
    Context context;
    ArrayList<String> itemList;
    OptionChosen optionChosen;

    public BrandsListAdapter(Context context, ArrayList<String> itemList, OptionChosen deleteBrand) {
        this.context = context;
        this.itemList = itemList;
        this.optionChosen = deleteBrand;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.brand_item_layout, parent, false);
        BrandsListAdapter.ViewHolder viewHolder = new BrandsListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String name = itemList.get(position);
        holder.brandName.setText((position + 1) + " - " + name);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionChosen.onDeleteClick(name);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionChosen.onBrandChosen(name);

            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView brandName;
        ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            brandName = itemView.findViewById(R.id.brandName);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface OptionChosen {
        public void onDeleteClick(String id);

        public void onBrandChosen(String id);
    }
}
