package com.example.ccreview.viewModels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ccreview.models.Resolution;
import com.example.ccreview.utils.Constants;
import com.example.ccreview.utils.EmojiUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResolutionsViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Resolution>> mutableLiveDataResolutions = new MutableLiveData<>();
    String restaurantName;
    ArrayList<Resolution> resolutions = new ArrayList<>();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
            .child("Resolutions");

    public ResolutionsViewModel() {

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
                        resolutions.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Log.d(Constants.TAG, "onDataChange: " + child);
                            Resolution resolution = child.getValue(Resolution.class);
                            resolutions.add(resolution);
                        }
                        mutableLiveDataResolutions.setValue(resolutions);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void addEmojiReact(Resolution resolutionClicked, int emojisId) {
        ArrayList<String> emojis = resolutionClicked.getEmojiArrayList();
        emojis.add(EmojiUtils.getEmojiName(emojisId));
        databaseReference
                .child(restaurantName)
                .child(resolutionClicked.getResolutionId())
                .setValue(resolutionClicked);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
