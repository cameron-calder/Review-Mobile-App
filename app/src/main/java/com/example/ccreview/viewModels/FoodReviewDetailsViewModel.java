package com.example.ccreview.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ccreview.models.Restaurant;

public class FoodReviewDetailsViewModel extends ViewModel {

    public MutableLiveData<Restaurant> restaurant = new MutableLiveData<>();

    public FoodReviewDetailsViewModel() { }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant.setValue(restaurant);
    }
}
