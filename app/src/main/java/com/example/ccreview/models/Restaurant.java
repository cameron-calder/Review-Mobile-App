package com.example.ccreview.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable {
    String name;
    String address;
    float rating;
    String thumbnailURL;

    public Restaurant(String name, String address, float rating, String thumbnailURL) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.thumbnailURL = thumbnailURL;
    }

    public Restaurant() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float thumbnail) {
        this.rating = thumbnail;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeFloat(this.rating);
        dest.writeString(this.thumbnailURL);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.address = source.readString();
        this.rating = source.readFloat();
        this.thumbnailURL = source.readString();
    }

    protected Restaurant(Parcel in) {
        this.name = in.readString();
        this.address = in.readString();
        this.rating = in.readFloat();
        this.thumbnailURL = in.readString();
    }

    public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel source) {
            return new Restaurant(source);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", rating=" + rating +
                ", thumbnailURL='" + thumbnailURL + '\'' +
                '}';
    }
}
