package com.example.ccreview.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.ccreview.utils.Constants;

import java.util.ArrayList;

public class Review implements Parcelable {
    String title, description, userName;
    long createdAt;
    float rating;
    int numberOfLikes, numberOfDisLikes;
    Reply reply;
    ArrayList<String> emojiArrayList;
    String reviewId;
    String restaurantName;
    String imageURL;
    String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Review() {
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Review(String title, String description, String userName, float rating, String restaurantName) {
        this.title = title;
        this.description = description;
        this.userName = userName;
        this.rating = rating;
        this.numberOfDisLikes = 0;
        this.numberOfLikes = 0;
        this.restaurantName = restaurantName;
        createdAt = System.currentTimeMillis();
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
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

    public boolean isShowEmojis() {
        if (emojiArrayList != null) {
            Log.d(Constants.TAG, "isShowEmojis: emojiArrayList not null"  );
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public int getNumberOfDisLikes() {
        return numberOfDisLikes;
    }

    public void setNumberOfDisLikes(int numberOfDisLikes) {
        this.numberOfDisLikes = numberOfDisLikes;
    }

    public Reply getReply() {
        return reply;
    }

    public void setReply(Reply reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return "Review{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", userName='" + userName + '\'' +
                ", createdAt=" + createdAt +
                ", rating=" + rating +
                ", numberOfLikes=" + numberOfLikes +
                ", numberOfDisLikes=" + numberOfDisLikes +
                ", reply=" + reply +
                ", emojiArrayList=" + (emojiArrayList != null ? emojiArrayList.size() : null)+
                ", reviewId='" + reviewId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.userName);
        dest.writeLong(this.createdAt);
        dest.writeFloat(this.rating);
        dest.writeInt(this.numberOfLikes);
        dest.writeInt(this.numberOfDisLikes);
        dest.writeParcelable(this.reply, flags);
        dest.writeStringList(this.emojiArrayList);
        dest.writeString(this.reviewId);
        dest.writeString(this.restaurantName);
    }

    public void readFromParcel(Parcel source) {
        this.title = source.readString();
        this.description = source.readString();
        this.userName = source.readString();
        this.createdAt = source.readLong();
        this.rating = source.readFloat();
        this.numberOfLikes = source.readInt();
        this.numberOfDisLikes = source.readInt();
        this.reply = source.readParcelable(Reply.class.getClassLoader());
        this.emojiArrayList = source.createStringArrayList();
        this.reviewId = source.readString();
        this.restaurantName = source.readString();
    }

    protected Review(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        this.userName = in.readString();
        this.createdAt = in.readLong();
        this.rating = in.readFloat();
        this.numberOfLikes = in.readInt();
        this.numberOfDisLikes = in.readInt();
        this.reply = in.readParcelable(Reply.class.getClassLoader());
        this.emojiArrayList = in.createStringArrayList();
        this.reviewId = in.readString();
        this.restaurantName = in.readString();
    }

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
