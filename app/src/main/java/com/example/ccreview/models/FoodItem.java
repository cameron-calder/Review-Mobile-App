package com.example.ccreview.models;

public class FoodItem {
    int imageId;
    String title;
    float rating;
    String description;

    public FoodItem(int imageId) {
        this.imageId = imageId;
    }

    public FoodItem() {
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FoodItem(int imageId, String title, float rating, String description) {
        this.imageId = imageId;
        this.title = title;
        this.rating = rating;
        this.description = description;
    }
}
