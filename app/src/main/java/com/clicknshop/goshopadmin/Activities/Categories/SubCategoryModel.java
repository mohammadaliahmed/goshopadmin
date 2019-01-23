package com.clicknshop.goshopadmin.Activities.Categories;

public class SubCategoryModel {
    String subCategoryName,mainCategory,picUrl;
    int position;


    public SubCategoryModel() {
    }

    public SubCategoryModel(String subCategoryName, String mainCategory, String picUrl,int position) {
        this.subCategoryName = subCategoryName;
        this.mainCategory = mainCategory;
        this.picUrl = picUrl;
        this.position=position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
