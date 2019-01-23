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

import com.clicknshop.goshopadmin.R;

import java.util.ArrayList;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.ViewHolder> {
    Context context;
    ArrayList<MainCategoryModel> itemList;
    int flag;
    int secondFlag;

    ChooseOption chooseOption;

    public CategoriesListAdapter(Context context, ArrayList<MainCategoryModel> itemList, int flag, int secondFlag, ChooseOption chooseOption) {
        this.context = context;
        this.itemList = itemList;
        this.flag = flag;
        this.secondFlag = secondFlag;
        this.chooseOption = chooseOption;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item_layout, parent, false);
        CategoriesListAdapter.ViewHolder viewHolder = new CategoriesListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MainCategoryModel category = itemList.get(position);
        holder.category.setText("Category: " + category.getMainCategory() + "\nPosition: " + category.getPosition());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddSubCategory.class);
                i.putExtra("mainCategory", category.getMainCategory());
                i.putExtra("position", category.getPosition());
//                i.putExtra("category",category);
                context.startActivity(i);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseOption.deleteCategory(category.getMainCategory());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            category = itemView.findViewById(R.id.category);
        }
    }

    public interface ChooseOption {
        public void deleteCategory(String category);
    }
}
