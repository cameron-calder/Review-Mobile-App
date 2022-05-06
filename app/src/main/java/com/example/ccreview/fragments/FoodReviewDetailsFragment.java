package com.example.ccreview.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.ccreview.R;
import com.example.ccreview.adapters.MyViewPagerAdapter;
import com.example.ccreview.databinding.FragmentFoodReviewDetailsBinding;
import com.example.ccreview.models.Restaurant;
import com.example.ccreview.utils.Constants;
import com.example.ccreview.viewModels.FoodReviewDetailsViewModel;
import com.google.android.material.tabs.TabLayoutMediator;

public class FoodReviewDetailsFragment extends Fragment {

    FragmentFoodReviewDetailsBinding binding;
    String[] titlesArray;
    int[] iconsArray;
    Fragment[] fragments;
    MyViewPagerAdapter myViewPagerAdapter;
    Restaurant restaurant;
    FoodReviewDetailsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFoodReviewDetailsBinding.inflate(getLayoutInflater());

        Log.d(Constants.TAG, "onCreateView: " + restaurant);

        init();
        populateViews();
        setListeners();

        return binding.getRoot();
    }

    private void populateViews() {
        binding.tvTitle.setText(restaurant.getName());
        binding.tvDesc.setText(restaurant.getAddress());
        Glide.with(requireContext())
                .load(restaurant.getThumbnailURL())
                .placeholder(R.drawable.placeholder_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(binding.ivThumbnail);
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
    }

    private void init() {
        restaurant = FoodReviewDetailsFragmentArgs.fromBundle(getArguments()).getRestaurant();
        viewModel = new ViewModelProvider(requireActivity()).get(FoodReviewDetailsViewModel.class);
        viewModel.setRestaurant(restaurant);
        titlesArray = new String[]{"Reviews", "Resolution"};
        iconsArray = new int[]{R.drawable._chat_4_fill, R.drawable.ticksvg};
        fragments = new Fragment[]{new TabReviewFragment(), new TabResolutionsFragment()};

        myViewPagerAdapter = new MyViewPagerAdapter(requireActivity(), titlesArray, fragments);
        binding.viewPager.setAdapter(myViewPagerAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    tab.setText(titlesArray[position]);
                    tab.setIcon(iconsArray[position]);


                }).attach();
    }
}