package com.example.ccreview.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.ccreview.databinding.FragmentAddReviewBinding;
import com.example.ccreview.databinding.FragmentEditReviewBinding;
import com.example.ccreview.models.Review;
import com.example.ccreview.viewModels.CommentsViewModel;

public class EditReviewFragment extends Fragment {

    FragmentEditReviewBinding binding;
    Review review;
    CommentsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditReviewBinding.inflate(getLayoutInflater());
        init();
        populateViews();
        setListeners();
        return binding.getRoot();
    }

    private void populateViews() {
        binding.materialRatingBar.setRating(review.getRating());
        binding.etTitle.setText(review.getTitle());
        binding.etFeedback.setText(review.getDescription());
    }

    private void setListeners() {
        binding.btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editReview();
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
    }

    private void editReview() {
    }

    private void init() {
        viewModel = new ViewModelProvider(requireActivity()).get(CommentsViewModel.class);
        review = EditReviewFragmentArgs.fromBundle(getArguments()).getReview();
    }

//    private void addReview() {
//        String title = binding.etTitle.getText().toString().trim();
//        String feedback = binding.etFeedback.getText().toString().trim();
//        float rating = binding.materialRatingBar.getRating();
//        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
//        Review review = new Review(title, feedback, username, rating, restaurant.getName());
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
//                .child("Reviews")
//                .child(restaurant.getName())
//                .push();
//        review.setReviewId(databaseReference.getKey());
//
//        databaseReference.setValue(review)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Navigation.findNavController(binding.btnAddReview).navigateUp();
//                        Toast.makeText(requireContext(), "Review Added", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//    }
}