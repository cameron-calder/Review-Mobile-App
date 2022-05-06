package com.example.ccreview.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.ccreview.R;
import com.example.ccreview.databinding.ItemFoodRecyclerViewBinding;
import com.example.ccreview.models.Restaurant;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FoodIRecyclerViewAdapter extends RecyclerView.Adapter<FoodIRecyclerViewAdapter.MyViewHolder> {

    ArrayList<Restaurant> restaurantArrayList;
    Context context;
    OnFoodItemClickListener onFoodItemClickListener;

    public void setOnFoodItemClickListener(OnFoodItemClickListener onFoodItemClickListener) {
        this.onFoodItemClickListener = onFoodItemClickListener;
    }

    public FoodIRecyclerViewAdapter(ArrayList<Restaurant> restaurantArrayList, Context context) {
        this.restaurantArrayList = restaurantArrayList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemFoodRecyclerViewBinding binding = ItemFoodRecyclerViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        Restaurant restaurant = restaurantArrayList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFoodItemClickListener != null) {
                    onFoodItemClickListener.onFoodItemClicked(restaurant, holder.getAdapterPosition());
                }
            }
        });

        holder.binding.tvTitle.setText(restaurant.getName());
        holder.binding.tvDesc.setText(restaurant.getAddress());
        holder.binding.tvRating.setText(String.valueOf(restaurant.getRating()));
        Glide.with(context)
                .load(restaurant.getThumbnailURL())
                .placeholder(R.drawable.placeholder_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(holder.binding.ivThumbnail);

    }

    @Override
    public int getItemCount() {
        return restaurantArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemFoodRecyclerViewBinding binding;

        public MyViewHolder(@NonNull @NotNull ItemFoodRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnFoodItemClickListener {
        void onFoodItemClicked(Restaurant restaurant, int position);
    }
}
