package com.clicknshop.goshopadmin.Models;

public class ProductUploadModel {
     String title,subtitle,price,category;

    public ProductUploadModel(String title, String subtitle, String price, String category) {
        this.title = title;
        this.subtitle = subtitle;
        this.price = price;
        this.category = category;
    }

    public ProductUploadModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
