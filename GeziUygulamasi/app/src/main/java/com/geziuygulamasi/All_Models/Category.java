package com.geziuygulamasi.All_Models;

import java.io.Serializable;

public class Category implements Serializable {
    private final int categoryId;
    private final String categoryImage, categoryText;

    public Category(int categoryId, String categoryImage, String categoryText) {
        this.categoryId = categoryId;
        this.categoryImage = categoryImage;
        this.categoryText = categoryText;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public String getCategoryText() {
        return categoryText;
    }
}