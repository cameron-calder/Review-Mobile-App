package com.example.ccreview.models;

import android.util.Log;

import com.example.ccreview.utils.Constants;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class Resolution {
    String title, userName;
    float rating;
    String message;
    long createdAt;
    String resolutionId;
    String restaurantName;

    ArrayList<String> emojiArrayList;

    public Resolution() {
        createdAt = System.currentTimeMillis();

    }

    public Resolution(String title, String userName, float rating, String restaurantName) {
        this.title = title;
        this.userName = userName;
        this.rating = rating;
        this.restaurantName = restaurantName;
        this.message = "Received a full refund and been issued a free gift for their next visit";
        createdAt = System.currentTimeMillis();
    }

    @Exclude
    public boolean isShowEmojis() {
        if (emojiArrayList != null) {
            Log.d(Constants.TAG, "isShowEmojis: emojiArrayList not null");
            return !emojiArrayList.isEmpty();
        }
        return false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getResolutionId() {
        return resolutionId;
    }

    public void setResolutionId(String resolutionId) {
        this.resolutionId = resolutionId;
    }

    public ArrayList<String> getEmojiArrayList() {
        if(emojiArrayList == null){
            emojiArrayList = new ArrayList<>();
        }
        return emojiArrayList;
    }

    public void setEmojiArrayList(ArrayList<String> emojiArrayList) {
        this.emojiArrayList = emojiArrayList;
    }
}
