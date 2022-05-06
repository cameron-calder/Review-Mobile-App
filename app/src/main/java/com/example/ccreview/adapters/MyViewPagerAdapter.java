package com.example.ccreview.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    String[] titlesArray;
    Fragment[] fragments;

    public MyViewPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity, String[] titlesArray, Fragment[] fragments) {

        super(fragmentActivity);
        this.titlesArray = titlesArray;
        this.fragments = fragments;

    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return titlesArray.length;
    }
}