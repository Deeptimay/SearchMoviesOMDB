package com.example.searchmoviesomdb.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.example.searchmoviesomdb.R;
import com.example.searchmoviesomdb.Utils.GlideApp;

public class BindingAdapters {

    @BindingAdapter({"imageUrl", "isBackdrop"})
    public static void bindImage(ImageView imageView, String imagePath, boolean isBackdrop) {
        GlideApp.with(imageView.getContext())
                .load(imagePath)
                .placeholder(R.drawable.ic_cinema)
                .into(imageView);
    }

    /**
     * Movie details poster image
     */
    @BindingAdapter({"imageUrl"})
    public static void bindImage(ImageView imageView, String imagePath) {
        GlideApp.with(imageView.getContext())
                .load(imagePath)
                .placeholder(R.drawable.ic_cinema)
                .into(imageView);
    }

    @BindingAdapter("visibleGone")
    public static void showHide(View view, Boolean show) {
        if (show) view.setVisibility(View.VISIBLE);
        else view.setVisibility(View.GONE);
    }
}
