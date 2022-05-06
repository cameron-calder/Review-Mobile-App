package com.example.ccreview.models;

import com.example.ccreview.utils.EmojiUtils;

public class Emoji {
    @EmojiType
    String emojiType;
    int resourceId;

    public Emoji() {
    }

    public String getEmojiType() {
        return emojiType;
    }

    public void setEmojiType(String emojiType) {
        this.emojiType = emojiType;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public Emoji(String emojiType) {
        this.emojiType = emojiType;
        this.resourceId = EmojiUtils.getEmojiResourceId(emojiType);
    }

    public Emoji(int resourceId) {
        this.resourceId = resourceId;
        this.emojiType = EmojiUtils.getEmojiName(resourceId);
    }
}
