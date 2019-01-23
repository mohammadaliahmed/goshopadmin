package com.clicknshop.goshopadmin.Activities.Categories;

public class MainCategoryModel {
    String mainCategory;
    int position;

    public MainCategoryModel() {
    }

    public MainCategoryModel(String mainCategory, int position) {
        this.mainCategory = mainCategory;
        this.position = position;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
