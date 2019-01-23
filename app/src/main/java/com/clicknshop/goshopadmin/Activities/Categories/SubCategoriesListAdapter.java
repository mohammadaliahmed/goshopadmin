package com.clicknshop.goshopadmin.Activities.Categories;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clicknshop.goshopadmin.R;

import java.util.ArrayList;

public class SubCategoriesListAdapter extends RecyclerView.Adapter<SubCategoriesListAdapter.ViewHolder> {
    Context context;
    ArrayList<SubCategoryModel> itemList;
    int flag;
    int secondFlag;

    ChooseOption chooseOption;

    public SubCategoriesListAdapter(Context context, ArrayList<SubCategoryModel> itemList, int flag, int secondFlag, ChooseOption chooseOption) {
        this.context = context;
        this.itemList = itemList;
        this.flag = flag;
        this.secondFlag = secondFlag;
        this.chooseOption = chooseOption;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_category_item_layout, parent, false);
        SubCategoriesListAdapter.ViewHolder viewHolder = new SubCategoriesListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SubCategoryModel category = itemList.get(position);
        holder.category.setText(category.getSubCategoryName()
                + "\nPosition: " + category.getPosition()
        );
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddChildCategories.class);
                i.putExtra("subCategory", category.getSubCategoryName());
                context.startActivity(i);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseOption.deleteCategory(category.getMainCategory(), category.subCategoryName);
            }
        });
        Glide.with(context).load(category.getPicUrl()).into(holder.categoryImage);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        ImageView delete, categoryImage;

        public ViewHolder(View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            category = itemView.findViewById(R.id.category);
            categoryImage = itemView.findViewById(R.id.categoryImage);
        }
    }

    public interface ChooseOption {
        public void deleteCategory(String mainCategory, String subCategory);
    }
}
