package com.example.ccreview.models;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        EmojiType.THUMBS_UP,
        EmojiType.THUMBS_DOWN,
        EmojiType.LAUGHING_TEARS,
        EmojiType.HEART,
        EmojiType.CRYING,
        EmojiType.LAUGHING_MOUTH_OPEN,
        EmojiType.HEART_EYES,
        EmojiType.DISGUSTED,
        EmojiType.ANGRY
})
@Retention(RetentionPolicy.SOURCE)
public @interface EmojiType {
    String THUMBS_UP = "THUMBS_UP";
    String THUMBS_DOWN = "THUMBS_DOWN";
    String LAUGHING_TEARS = "LAUGHING_TEARS";
    String HEART = "HEART";
    String CRYING = "CRYING";
    String LAUGHING_MOUTH_OPEN = "LAUGHING_MOUTH_OPEN";
    String HEART_EYES = "HEART_EYES";
    String DISGUSTED = "DISGUSTED";
    String ANGRY = "ANGRY";

}
