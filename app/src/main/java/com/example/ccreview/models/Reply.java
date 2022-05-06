package com.example.ccreview.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Reply implements Parcelable {
    String replyTime, replyMessage, repliedBy;

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public String getRepliedBy() {
        return repliedBy;
    }

    public void setRepliedBy(String repliedBy) {
        this.repliedBy = repliedBy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.replyTime);
        dest.writeString(this.replyMessage);
        dest.writeString(this.repliedBy);
    }

    public void readFromParcel(Parcel source) {
        this.replyTime = source.readString();
        this.replyMessage = source.readString();
        this.repliedBy = source.readString();
    }

    public Reply() {
    }

    protected Reply(Parcel in) {
        this.replyTime = in.readString();
        this.replyMessage = in.readString();
        this.repliedBy = in.readString();
    }

    public static final Parcelable.Creator<Reply> CREATOR = new Parcelable.Creator<Reply>() {
        @Override
        public Reply createFromParcel(Parcel source) {
            return new Reply(source);
        }

        @Override
        public Reply[] newArray(int size) {
            return new Reply[size];
        }
    };
}
