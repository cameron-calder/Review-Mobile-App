package com.example.ccreview.fragments;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.ccreview.R;
import com.example.ccreview.databinding.FragmentAddReviewBinding;
import com.example.ccreview.models.Resolution;
import com.example.ccreview.models.Restaurant;
import com.example.ccreview.models.Review;
import com.example.ccreview.utils.Constants;
import com.example.ccreview.utils.DialogUtil;
import com.example.ccreview.viewModels.CommentsViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class AddReviewFragment extends Fragment {

    private static final int REQUEST_CODE_PERMISSIONS = 10;
    FragmentAddReviewBinding binding;
    Restaurant restaurant;
    CommentsViewModel viewModel;
    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    Uri photoUri;
    String imageURL = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddReviewBinding.inflate(getLayoutInflater());
        init();

        binding.btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoUri != null) {
                    uploadImage();
                } else {
                    addReview();
                }
            }
        });
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });

        binding.cbLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                binding.tilLocation.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });

        binding.btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasStoragePermissions()) {
                    pickImage();
                } else {
                    getStoragePermissions();
                }
            }
        });

        binding.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.layoutAttachment.setVisibility(View.GONE);
                photoUri = null;
            }
        });
        return binding.getRoot();
    }

    private void uploadImage() {
        DialogUtil.showSimpleProgressDialog(requireContext());
        if (photoUri != null) {
            String fileName = System.currentTimeMillis() + "_review_photo.jpg";
            StorageReference storageReference = FirebaseStorage.getInstance()
                    .getReference()
                    .child("attachments")
                    .child(fileName);
            storageReference.putFile(photoUri)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            Log.d(Constants.TAG, "then: response");
                            if (!task.isSuccessful()) {
                                Log.d(Constants.TAG, "then: exception" + task.getException().getMessage());
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return storageReference.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        imageURL = String.valueOf(task.getResult());
                        addReview();
                    } else {
                        DialogUtil.closeProgressDialog();
                        Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void init() {
        viewModel = new ViewModelProvider(requireActivity()).get(CommentsViewModel.class);
        restaurant = AddReviewFragmentArgs.fromBundle(getArguments()).getRestaurant();
        binding.tvTitle.setText(restaurant.getName());
        binding.tvDesc.setText(restaurant.getAddress());
        Glide.with(requireContext())
                .load(restaurant.getThumbnailURL())
                .placeholder(R.drawable.placeholder_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(binding.ivThumbnail);
    }

    void pickImage() {
        ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(60)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }


    boolean hasStoragePermissions() {
        return EasyPermissions.hasPermissions(requireContext(),
                permissions);
    }

    private void getStoragePermissions() {
        EasyPermissions.requestPermissions(
                this,
                "Storage permissions are required",
                REQUEST_CODE_PERMISSIONS,
                permissions
        );
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSIONS)
    private void methodRequiresTwoPermission() {
        if (EasyPermissions.hasPermissions(requireContext(), permissions)) {
            // Already have permission,
            pickImage();
        } else {
            // Do not have permissions, request them now
            getStoragePermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                photoUri = data.getData();
                if (photoUri != null) {
                    binding.layoutAttachment.setVisibility(View.VISIBLE);
                    Glide.with(requireContext())
                            .load(photoUri)
                            .placeholder(R.drawable.placeholder_image)
                            .transition(withCrossFade())
                            .error(R.drawable.avatar)
                            .into(binding.ivAttachment);
                }
            }
        }
    }

    private void addReview() {

        String title = binding.etTitle.getText().toString().trim();
        String feedback = binding.etFeedback.getText().toString().trim();
        float rating = binding.materialRatingBar.getRating();
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        Review review = new Review(title, feedback, username, rating, restaurant.getName());
        if (!imageURL.isEmpty()) {
            review.setImageURL(imageURL);
        }
        if (binding.cbLocation.isChecked()) {
            String location = binding.etLocation.getText().toString().trim();
            review.setLocation(location);
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Reviews")
                .child(restaurant.getName())
                .push();
        review.setReviewId(databaseReference.getKey());

        if (review.getRating() <= 1) {
            Resolution resolution = new Resolution(title, username, rating, restaurant.getName());

            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference()
                    .child("Resolutions")
                    .child(restaurant.getName())
                    .push();

            resolution.setResolutionId(databaseReference1.getKey());
            databaseReference1.setValue(resolution);
        }
        databaseReference.setValue(review)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DialogUtil.closeProgressDialog();
                        Navigation.findNavController(binding.btnAddReview).navigateUp();
                        Toast.makeText(requireContext(), "Review Added", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}