package com.example.ccreview.utils;

import com.example.ccreview.R;
import com.example.ccreview.models.EmojiType;

public class EmojiUtils {

    public static int getEmojiResourceId(@EmojiType String emoji) {
        int id = R.drawable.frame_4;
        switch (emoji) {
            case EmojiType.THUMBS_UP:
                id = R.drawable.frame_4;
                break;
            case EmojiType.THUMBS_DOWN:
                id = R.drawable.frame_5;
                break;
            case EmojiType.LAUGHING_TEARS:
                id = R.drawable.emoji;
                break;
            case EmojiType.HEART:
                id = R.drawable.red_heart;
                break;
            case EmojiType.CRYING:
                id = R.drawable.crying_face;
                break;
            case EmojiType.LAUGHING_MOUTH_OPEN:
                id = R.drawable.emoji_1;
                break;
            case EmojiType.HEART_EYES:
                id = R.drawable.emoji_2;
                break;
            case EmojiType.DISGUSTED:
                id = R.drawable.emoji_3;
                break;
            case EmojiType.ANGRY:
                id = R.drawable.emoji_4;
                break;
        }

        return id;
    }

    public static String getEmojiName(int id) {
        String emojiName = EmojiType.THUMBS_UP;
        switch (id) {
            case R.drawable.frame_4:
                emojiName = EmojiType.THUMBS_UP;
                break;
            case R.drawable.frame_5:
                emojiName = EmojiType.THUMBS_DOWN;
                break;
            case R.drawable.emoji:
                emojiName = EmojiType.LAUGHING_TEARS;
                break;
            case R.drawable.red_heart:
                emojiName = EmojiType.HEART;
                break;
            case R.drawable.crying_face:
                emojiName = EmojiType.CRYING;
                break;
            case R.drawable.emoji_1:
                emojiName = EmojiType.LAUGHING_MOUTH_OPEN;
                break;
            case R.drawable.emoji_2:
                emojiName = EmojiType.HEART_EYES;
                break;
            case R.drawable.emoji_3:
                emojiName = EmojiType.DISGUSTED;
                break;
            case R.drawable.emoji_4:
                emojiName = EmojiType.ANGRY;
                break;
        }

        return emojiName;
    }
}
