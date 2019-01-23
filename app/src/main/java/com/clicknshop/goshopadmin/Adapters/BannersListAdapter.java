package com.clicknshop.goshopadmin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.clicknshop.goshopadmin.Models.BannerModel;
import com.clicknshop.goshopadmin.R;

import java.util.ArrayList;

public class BannersListAdapter extends RecyclerView.Adapter<BannersListAdapter.ViewHolder> {
    Context context;
    ArrayList<BannerModel> itemList;
    DeleteBanner deleteBanner;

    public BannersListAdapter(Context context, ArrayList<BannerModel> itemList, DeleteBanner deleteBanner) {
        this.context = context;
        this.itemList = itemList;
        this.deleteBanner = deleteBanner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item_layout, parent, false);
        BannersListAdapter.ViewHolder viewHolder = new BannersListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final BannerModel model = itemList.get(position);
        Glide.with(context).load(model.getBannerUrl()).into(holder.image);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBanner.onDeleteClicked(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView delete, image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    public interface DeleteBanner {
        public void onDeleteClicked(BannerModel model);
    }
}
