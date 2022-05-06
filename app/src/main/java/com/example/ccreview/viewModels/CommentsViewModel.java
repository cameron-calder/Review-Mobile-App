package com.example.ccreview.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ccreview.models.Review;
import com.example.ccreview.utils.Constants;
import com.example.ccreview.utils.EmojiUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentsViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Review>> mutableLiveDataReviews = new MutableLiveData<>();
    String restaurantName;
    ArrayList<Review> reviews = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
            .child("Reviews");

    public CommentsViewModel() {

    }

    public void setRestaurantName(String name) {
        Log.d(Constants.TAG, "setRestaurantName: " + name);
        this.restaurantName = name;
    }

    public void refreshReviewsData() {
        databaseReference
                .child(restaurantName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reviews.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Log.d(Constants.TAG, "onDataChange: " + child);
                            Review review = child.getValue(Review.class);
                            reviews.add(review);
                        }
                        mutableLiveDataReviews.setValue(reviews);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void addEmojiReact(Review reviewClicked, int emojisId) {
        ArrayList<String> emojis = reviewClicked.getEmojiArrayList();
        emojis.add(EmojiUtils.getEmojiName(emojisId));
        databaseReference
                .child(restaurantName)
                .child(reviewClicked.getReviewId())
                .setValue(reviewClicked);
    }

    @Override
    protected void onCleared() {
//        FirebaseDatabase.getInstance().getReference().child()
        super.onCleared();
    }
}
