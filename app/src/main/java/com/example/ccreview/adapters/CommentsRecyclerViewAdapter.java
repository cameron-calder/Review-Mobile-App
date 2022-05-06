package com.example.ccreview.adapters;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.ccreview.R;
import com.example.ccreview.databinding.ItemReviewRecyclerViewBinding;
import com.example.ccreview.models.Emoji;
import com.example.ccreview.models.Review;
import com.example.ccreview.utils.Constants;
import com.example.ccreview.utils.EmojiUtils;
import com.example.ccreview.utils.DateTimeUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.MyViewHolder> {

    ArrayList<Review> reviewArrayList;
    Context context;
    OnCommentClickListener onCommentClickListener;

    public CommentsRecyclerViewAdapter(ArrayList<Review> reviewArrayList, Context context) {
        this.reviewArrayList = reviewArrayList;
        this.context = context;
    }

    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        this.onCommentClickListener = onCommentClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemReviewRecyclerViewBinding binding = ItemReviewRecyclerViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        Review review = reviewArrayList.get(holder.getAdapterPosition());

        Log.d(Constants.TAG, "onBindViewHolder: Review: " + review.toString());
        holder.binding.materialRatingBar.setRating(review.getRating());
        holder.binding.tvTimeAgo.setText(DateTimeUtils.getTimeAgo(review.getCreatedAt()));
        holder.binding.tvHeadline.setText(review.getTitle());
        holder.binding.tvUsername.setText(review.getUserName());
        holder.binding.tvComment.setText(review.getDescription());
        holder.binding.tvComment.setText(review.getDescription());
        holder.binding.cbLike.setText(String.valueOf(review.getNumberOfLikes()));
        holder.binding.cbDislike.setText(String.valueOf(review.getNumberOfDisLikes()));

        if (review.getLocation() != null) {
            if (!review.getLocation().isEmpty()) {
                holder.binding.tvLocation.setText(review.getLocation());
                holder.binding.tvLocation.setVisibility(View.VISIBLE);
            }
        }
        if (review.getImageURL() != null) {
            if (!review.getImageURL().isEmpty()) {
                holder.binding.layoutAttachment.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(review.getImageURL())
                        .placeholder(R.drawable.placeholder_image)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(holder.binding.ivAttachment);
            }
        }

//        Log.d(Constants.TAG, "onBindViewHolder: " + review.isShowEmojis());
        if (review.isShowEmojis()) {
            ArrayList<String> emojis = review.getEmojiArrayList();
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
                if (onCommentClickListener != null) {
                    onCommentClickListener.onCommentClicked(holder.getAdapterPosition(), reviewArrayList.get(holder.getAdapterPosition()));
                }
            }
        });

    }

    private void addEmoji(LinearLayout llEmojis, String emoji) {
        ImageButton imageButton = new ImageButton(context);
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(
                android.R.attr.selectableItemBackground, outValue, true);
        imageButton.setBackground(AppCompatResources.getDrawable(context, outValue.resourceId));
        imageButton.setImageResource(EmojiUtils.getEmojiResourceId(emoji));
        llEmojis.addView(imageButton, llEmojis.getChildCount());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public interface OnCommentClickListener {
        void onCommentClicked(int position, Review review);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemReviewRecyclerViewBinding binding;

        public MyViewHolder(@NonNull @NotNull ItemReviewRecyclerViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
