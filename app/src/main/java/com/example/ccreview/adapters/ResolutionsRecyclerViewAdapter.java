package com.example.ccreview.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ccreview.databinding.ItemResolutionsRecyclerViewBinding;
import com.example.ccreview.databinding.ItemReviewRecyclerViewBinding;
import com.example.ccreview.models.Emoji;
import com.example.ccreview.models.Resolution;
import com.example.ccreview.utils.DateTimeUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResolutionsRecyclerViewAdapter extends RecyclerView.Adapter<ResolutionsRecyclerViewAdapter.MyViewHolder> {

    ArrayList<Resolution> resolutions;
    Context context;
    OnResolutionClickListener onResolutionClickListener;

    public ResolutionsRecyclerViewAdapter(ArrayList<Resolution> resolutions, Context context) {
        this.resolutions = resolutions;
        this.context = context;
    }

    public void setOnCommentClickListener(OnResolutionClickListener onCommentClickListener) {
        this.onResolutionClickListener = onCommentClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemResolutionsRecyclerViewBinding binding = ItemResolutionsRecyclerViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {

        Resolution resolution = resolutions.get(holder.getAdapterPosition());

        holder.binding.materialRatingBar.setRating(resolution.getRating());
        holder.binding.tvUsername.setText(resolution.getUserName());
        holder.binding.tvComment.setText(resolution.getTitle());
        holder.binding.tvDateTime.setText(DateTimeUtils.getFormattedDateTime(resolution.getCreatedAt()));
        if (resolution.isShowEmojis()) {
            ArrayList<String> emojis = resolution.getEmojiArrayList();
            // Create a list with the distinct elements using stream.
            List<String> emojisDistinct = emojis.stream().distinct().collect(Collectors.toList());
            ArrayList<Emoji> emojiArrayList = new ArrayList<>();
            for (String s : emojisDistinct) {
                Emoji emoji = new Emoji(s);
                emojiArrayList.add(emoji);
            }
            EmojiAdapter emojiAdapter = new EmojiAdapter(context, emojiArrayList);
            holder.binding.recyclerViewEmojis.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, true));
            holder.binding.recyclerViewEmojis.setAdapter(emojiAdapter);
        }

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onResolutionClickListener != null) {
                    onResolutionClickListener.onResolutionClickedClicked(holder.getAdapterPosition(), resolutions.get(holder.getAdapterPosition()));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return resolutions.size();
    }

    public interface OnResolutionClickListener {
        void onResolutionClickedClicked(int position, Resolution resolution);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemResolutionsRecyclerViewBinding binding;

        public MyViewHolder(@NonNull @NotNull ItemResolutionsRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }
}
