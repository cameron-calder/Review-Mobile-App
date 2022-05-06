package com.example.ccreview.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ccreview.models.Review;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllReviewsViewModel extends ViewModel {

    public MutableLiveData<ArrayList<Review>> reviewsMutableLiveData = new MutableLiveData<>();


    public AllReviewsViewModel() {
        setReviewsDataListener();
    }

    public void setReviewsDataListener() {
        FirebaseDatabase.getInstance().getReference()
                .child("Reviews")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Review> reviewArrayList = new ArrayList<>();
                        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                        for (DataSnapshot restaurantSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot reviewSnapshot : restaurantSnapshot.getChildren()) {
                                Review review = reviewSnapshot.getValue(Review.class);
                                if (review.getUserName().equals(userName)) {
                                    reviewArrayList.add(review);
                                }
                            }
                        }
                        reviewsMutableLiveData.setValue(reviewArrayList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void editComment(Review review, int position){
//        FirebaseDatabase.getInstance().getReference()
//                .child("Reviews")
//                .child(review.getRestaurantName())
//                .child()
    }

    public Task<Void> deleteComment(int position, Review review) {
        return FirebaseDatabase.getInstance().getReference()
                .child("Reviews")
                .child(review.getRestaurantName())
                .child(review.getReviewId())
                .removeValue();
    }
}
