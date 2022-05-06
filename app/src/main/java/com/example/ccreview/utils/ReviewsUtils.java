package com.example.ccreview.utils;

import com.example.ccreview.models.Review;

import java.util.ArrayList;

public class ReviewsUtils {

    public static float getRatingFromReviews(ArrayList<Review> reviews) {
        int sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return sum / (float) reviews.size();
    }

    public static int getTotalLikesFromReviews(ArrayList<Review> reviewArrayList) {
        int sum = 0;
        for (Review review : reviewArrayList) {
            sum += review.getNumberOfLikes();
        }
        return sum;
    }
}
