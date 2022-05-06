package com.example.ccreview.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ccreview.R;
import com.example.ccreview.adapters.ResolutionsRecyclerViewAdapter;
import com.example.ccreview.databinding.BottomSheetCommentBinding;
import com.example.ccreview.databinding.FragmentTabResolutionsBinding;
import com.example.ccreview.databinding.FragmentTabReviewBinding;
import com.example.ccreview.models.Resolution;
import com.example.ccreview.utils.Constants;
import com.example.ccreview.viewModels.FoodReviewDetailsViewModel;
import com.example.ccreview.viewModels.ResolutionsViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;


public class TabResolutionsFragment extends Fragment {


    FragmentTabResolutionsBinding binding;
    ResolutionsRecyclerViewAdapter adapter;
    ArrayList<Resolution> arrayList;

    FoodReviewDetailsViewModel foodReviewDetailsViewModel;
    ResolutionsViewModel resolutionViewModel;
    Resolution resolutionClicked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTabResolutionsBinding.inflate(getLayoutInflater());
        init();
        initData();
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resolutionViewModel.refreshReviewsData();
            }
        });

        adapter.setOnCommentClickListener(new ResolutionsRecyclerViewAdapter.OnResolutionClickListener() {
            @Override
            public void onResolutionClickedClicked(int position, Resolution resolution) {
                resolutionClicked = resolution;
                showEmojiBottomSheet();
            }
        });
        return binding.getRoot();
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

        bottomSheetCommentBinding.llComment.setVisibility(View.GONE);
        bottomSheetCommentBinding.llResolution.setVisibility(View.VISIBLE);

        bottomSheetCommentBinding.tvCancel.setOnClickListener(onClickListener);

        for (int emojisId : Constants.EMOJIS_IDS) {
            ImageButton imageButton = getImageButton(emojisId);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resolutionViewModel.addEmojiReact(resolutionClicked, emojisId);
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

    private void init() {
        arrayList = new ArrayList<>();
        binding.swipeRefreshLayout.setRefreshing(true);
        foodReviewDetailsViewModel = new ViewModelProvider(requireActivity()).get(FoodReviewDetailsViewModel.class);
        resolutionViewModel = new ViewModelProvider(requireActivity()).get(ResolutionsViewModel.class);
        resolutionViewModel.setRestaurantName(foodReviewDetailsViewModel.restaurant.getValue().getName());
        adapter = new ResolutionsRecyclerViewAdapter(arrayList, requireContext());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void initData() {

        resolutionViewModel.refreshReviewsData();
        resolutionViewModel.mutableLiveDataResolutions.observe(getViewLifecycleOwner(), new Observer<ArrayList<Resolution>>() {
            @Override
            public void onChanged(ArrayList<Resolution> resolutions) {
                binding.swipeRefreshLayout.setRefreshing(false);
                arrayList.clear();
                arrayList.addAll(resolutions);
                adapter.notifyDataSetChanged();
            }
        });

//        arrayList.add(new Resolution());
//        arrayList.add(new Resolution());
//        arrayList.add(new Resolution());
//        arrayList.add(new Resolution());
//        arrayList.add(new Resolution());
//        arrayList.add(new Resolution());
    }
}