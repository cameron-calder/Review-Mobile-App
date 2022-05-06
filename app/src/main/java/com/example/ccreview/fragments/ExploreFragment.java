package com.example.ccreview.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ccreview.R;
import com.example.ccreview.adapters.FoodIRecyclerViewAdapter;
import com.example.ccreview.databinding.BottomSheetFilterBinding;
import com.example.ccreview.databinding.FragmentExploreBinding;
import com.example.ccreview.models.Restaurant;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {

    FragmentExploreBinding binding;
    ArrayList<Restaurant> restaurantItemArrayList;
    FoodIRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(getLayoutInflater());

        init();
        setListeners();
        return binding.getRoot();
    }

    private void setListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialog);
                                                     BottomSheetFilterBinding bottomSheetFilterBinding = BottomSheetFilterBinding.inflate(getLayoutInflater());
                                                     bottomSheetDialog.setContentView(bottomSheetFilterBinding.getRoot());
                                                     bottomSheetDialog.show();

                                                     bottomSheetFilterBinding.btnBack.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             bottomSheetDialog.dismiss();
                                                         }
                                                     });
                                                 }
                                             }
        );
        adapter.setOnFoodItemClickListener(new FoodIRecyclerViewAdapter.OnFoodItemClickListener() {
            @Override
            public void onFoodItemClicked(Restaurant restaurant, int position) {
                ExploreFragmentDirections.ActionExploreFragmentToFoodReviewDetailsFragment action
                        = ExploreFragmentDirections.actionExploreFragmentToFoodReviewDetailsFragment(restaurant);
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_bottom_nav)
                        .navigate(action);
            }
        });

    }

    private void init() {
        restaurantItemArrayList = new ArrayList<>();
        initData();
        adapter = new FoodIRecyclerViewAdapter(restaurantItemArrayList, requireContext());
        binding.rvLocalCuisine.setAdapter(adapter);
        binding.rvModeratelyPriced.setAdapter(adapter);
        binding.rvVegan.setAdapter(adapter);

        binding.rvLocalCuisine.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvModeratelyPriced.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvVegan.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void initData() {
        binding.swipeRefreshLayout.setRefreshing(true);
        FirebaseDatabase.getInstance()
                .getReference()
                .child("Restaurants")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        binding.swipeRefreshLayout.setRefreshing(false);
                        for (DataSnapshot child : snapshot.getChildren()) {
                            Restaurant restaurant = child.getValue(Restaurant.class);
                            restaurantItemArrayList.add(restaurant);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//        restaurantItemArrayList.add(new FoodItem(R.drawable.unsplash_08boynh_r_e));
//        restaurantItemArrayList.add(new FoodItem(R.drawable.unsplash_eeqbbemh9_c));
//        restaurantItemArrayList.add(new FoodItem(R.drawable.unsplash_fdlzbwip0am));
//        restaurantItemArrayList.add(new FoodItem(R.drawable.unsplash_hlncigvui4q));
//        restaurantItemArrayList.add(new FoodItem(R.drawable.unsplash_igfigp5onv0));
//        restaurantItemArrayList.add(new FoodItem(R.drawable.unsplash_w6ftfbpcs9i));
//        restaurantItemArrayList.add(new FoodItem(R.drawable.unsplash_08boynh_r_e));
    }
}