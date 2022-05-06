package com.example.ccreview.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ccreview.R;
import com.example.ccreview.adapters.CommentsRecyclerViewAdapter;
import com.example.ccreview.databinding.BottomSheetCommentBinding;
import com.example.ccreview.databinding.BottomSheetEditCommentBinding;
import com.example.ccreview.databinding.FragmentAccountBinding;
import com.example.ccreview.models.Review;
import com.example.ccreview.utils.DialogUtil;
import com.example.ccreview.utils.ReviewsUtils;
import com.example.ccreview.viewModels.AllReviewsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AccountFragment extends Fragment {

    CommentsRecyclerViewAdapter adapter;
    ArrayList<Review> reviewArrayList;
    FragmentAccountBinding binding;
    AllReviewsViewModel allReviewsViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(getLayoutInflater());
        allReviewsViewModel = new ViewModelProvider(this).get(AllReviewsViewModel.class);
        init();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        allReviewsViewModel.reviewsMutableLiveData.observe(getActivity(), new Observer<ArrayList<Review>>() {
            @Override
            public void onChanged(ArrayList<Review> reviews) {
                reviewArrayList.clear();
                reviewArrayList.addAll(reviews);
                updateMetaData();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void updateMetaData() {
        float rating = ReviewsUtils.getRatingFromReviews(reviewArrayList);
        int totalComments = reviewArrayList.size();
        int totalLikes = ReviewsUtils.getTotalLikesFromReviews(reviewArrayList);
        binding.tvRating.setText(String.valueOf(rating));
        binding.tvComments.setText(String.valueOf(totalComments));
        binding.tvLikes.setText(String.valueOf(totalLikes));
    }


    private void init() {
        binding.tvTitle.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        reviewArrayList = new ArrayList<>();
        initData();
        adapter = new CommentsRecyclerViewAdapter(reviewArrayList, requireContext());
        binding.rvReviews.setAdapter(adapter);
        adapter.setOnCommentClickListener(new CommentsRecyclerViewAdapter.OnCommentClickListener() {
            @Override
            public void onCommentClicked(int position, Review review) {
                showEmojiBottomSheet(position, review);
            }
        });
        binding.rvReviews.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void initData() {

    }

    private void showEmojiBottomSheet(int position, Review review) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialog);
        BottomSheetEditCommentBinding bottomSheetEditCommentBinding = BottomSheetEditCommentBinding.inflate(getLayoutInflater(), binding.getRoot(), false);
        bottomSheetDialog.setContentView(bottomSheetEditCommentBinding.getRoot());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        };

        bottomSheetEditCommentBinding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtil.showSimpleProgressDialog(requireContext());
                allReviewsViewModel.deleteComment(position, review)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                DialogUtil.closeProgressDialog();
                                if (task.isSuccessful()) {
                                    Toast.makeText(requireContext(), "Comment deleted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(), "Comment deletion failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                bottomSheetDialog.dismiss();

            }
        });
        bottomSheetEditCommentBinding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                AccountFragmentDirections.ActionAccountFragmentToEditReviewFragment action
                        = AccountFragmentDirections.actionAccountFragmentToEditReviewFragment(review);
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_bottom_nav)
                        .navigate(action);
            }
        });

        bottomSheetDialog.show();
    }
}