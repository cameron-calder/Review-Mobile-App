package com.example.ccreview.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ccreview.databinding.ItemEmojiLayoutBinding;
import com.example.ccreview.models.Emoji;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.MyViewHolder> {

    ArrayList<Emoji> emojiArrayList;
    Context context;
    OnEmojiClickListener onEmojiClickListener;

    public void setOnEmojiListener(OnEmojiClickListener onEmojiClickListener) {
        this.onEmojiClickListener = onEmojiClickListener;
    }

    public EmojiAdapter(Context context, ArrayList<Emoji> emojiArrayList) {
        this.context = context;
        this.emojiArrayList = emojiArrayList;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ItemEmojiLayoutBinding binding = ItemEmojiLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        Emoji emoji = emojiArrayList.get(holder.getAdapterPosition());
        holder.binding.ivEmoji.setImageResource(emoji.getResourceId());

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEmojiClickListener != null) {
                    onEmojiClickListener.onEmojiClicked(holder.getAdapterPosition(), emojiArrayList.get(holder.getAdapterPosition()));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return emojiArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemEmojiLayoutBinding binding;

        public MyViewHolder(@NotNull ItemEmojiLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }
    }

    public interface OnEmojiClickListener {
        void onEmojiClicked(int position, Emoji emoji);
    }
}