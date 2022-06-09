package com.geziuygulamasi.All_Models;

import java.io.Serializable;

public class Place implements Serializable {
    private final int categoryId, id;
    private final String content, image, price, title, workingHour;

    public Place(int categoryId, int id, String content, String image, String price, String title, String workingHour) {
        this.categoryId = categoryId;
        this.id = id;
        this.content = content;
        this.image = image;
        this.price = price;
        this.title = title;
        this.workingHour = workingHour;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getWorkingHour() {
        return workingHour;
    }
}