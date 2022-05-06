package com.example.ccreview.fragments;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ccreview.R;
import com.example.ccreview.adapters.CommentsRecyclerViewAdapter;
import com.example.ccreview.databinding.BottomSheetCommentBinding;
import com.example.ccreview.databinding.FragmentTabReviewBinding;
import com.example.ccreview.models.Review;
import com.example.ccreview.utils.Constants;
import com.example.ccreview.utils.ReviewsUtils;
import com.example.ccreview.viewModels.CommentsViewModel;
import com.example.ccreview.viewModels.FoodReviewDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class TabReviewFragment extends Fragment {


    FragmentTabReviewBinding binding;
    CommentsRecyclerViewAdapter adapter;
    ArrayList<Review> reviewArrayList;
    FoodReviewDetailsViewModel foodReviewDetailsViewModel;
    CommentsViewModel commentsViewModel;
    Review reviewClicked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTabReviewBinding.inflate(getLayoutInflater());


        init();
        initData();

        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentsViewModel.refreshReviewsData();
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodReviewDetailsFragmentDirections.ActionFoodReviewDetailsFragmentToAddReviewFragment action
                        = FoodReviewDetailsFragmentDirections.actionFoodReviewDetailsFragmentToAddReviewFragment(foodReviewDetailsViewModel.restaurant.getValue());
                Navigation.findNavController(v).navigate(action);
            }
        });


        adapter.setOnCommentClickListener(new CommentsRecyclerViewAdapter.OnCommentClickListener() {
            @Override
            public void onCommentClicked(int position, Review review) {
                reviewClicked = review;
                showEmojiBottomSheet();
            }
        });
    }

    private void showEmojiBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialog);
        BottomSheetCommentBinding bottomSheetCommentBinding = BottomSheetCommentBinding.inflate(getLayoutInflater(), binding.getRoot(), false);
        bottomSheetDialog.setContentView(bottomSheetCommentBinding.getRoot());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        };

        bottomSheetCommentBinding.tvReply.setOnClickListener(onClickListener);
        bottomSheetCommentBinding.tvShare.setOnClickListener(onClickListener);
        bottomSheetCommentBinding.tvComplain.setOnClickListener(onClickListener);

        for (int emojisId : Constants.EMOJIS_IDS) {
            ImageButton imageButton = getImageButton(emojisId);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentsViewModel.addEmojiReact(reviewClicked, emojisId);
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetCommentBinding.flEmojis.addView(imageButton);
        }

        bottomSheetDialog.show();
    }

    @NonNull
    private ImageButton getImageButton(int emojisId) {
        ImageButton imageButton = new ImageButton(requireContext());
        int iconSize = requireContext().getResources().getDimensionPixelSize(R.dimen.icon_size);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                iconSize, iconSize);
        imageButton.setLayoutParams(layoutParams);
        imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
        TypedValue outValue = new TypedValue();
        requireContext().getTheme().resolveAttribute(
                android.R.attr.selectableItemBackgroundBorderless, outValue, true);
        imageButton.setBackground(AppCompatResources.getDrawable(requireContext(), outValue.resourceId));
        imageButton.setImageResource(emojisId);
        return imageButton;
    }

    private void setBottomSheetListeners(BottomSheetCommentBinding bottomSheetCommentBinding) {
    }

    private void init() {
        reviewArrayList = new ArrayList<>();
        binding.swipeRefreshLayout.setRefreshing(true);
        foodReviewDetailsViewModel = new ViewModelProvider(requireActivity()).get(FoodReviewDetailsViewModel.class);
        commentsViewModel = new ViewModelProvider(requireActivity()).get(CommentsViewModel.class);
        commentsViewModel.setRestaurantName(foodReviewDetailsViewModel.restaurant.getValue().getName());
        adapter = new CommentsRecyclerViewAdapter(reviewArrayList, requireContext());
        binding.rvReviews.setAdapter(adapter);
        binding.rvReviews.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void initData() {

        commentsViewModel.refreshReviewsData();
        commentsViewModel.mutableLiveDataReviews.observe(getViewLifecycleOwner(), new Observer<ArrayList<Review>>() {
            @Override
            public void onChanged(ArrayList<Review> reviews) {
                binding.swipeRefreshLayout.setRefreshing(false);
                reviewArrayList.clear();
                reviewArrayList.addAll(reviews);
                adapter.notifyDataSetChanged();
                populateRestaurantRating();
            }
        });

//        Review review = new Review();
//        reviewArrayList.add(review);
//        review.setReply(new Reply());
//        reviewArrayList.add(new Review());
//        reviewArrayList.add(review);
//        reviewArrayList.add(new Review());
//        reviewArrayList.add(review);
//        reviewArrayList.add(new Review());
//        reviewArrayList.add(review);
    }

    private void populateRestaurantRating() {
        binding.tvNumberOfRatings.setText(reviewArrayList.size() + " reviews");
        float rating = ReviewsUtils.getRatingFromReviews(reviewArrayList);
        binding.tvRating.setText(String.valueOf(rating));
        binding.ratingBar.setRating(rating);
    }
}