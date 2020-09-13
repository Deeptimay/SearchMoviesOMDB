package com.example.searchmoviesomdb.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.searchmoviesomdb.callbacks.OnItemClickedListener;
import com.example.searchmoviesomdb.databinding.ListItemMovieBinding;
import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.ui.MovieDetailFragment;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    private final ListItemMovieBinding binding;

    public MovieViewHolder(@NonNull ListItemMovieBinding binding) {
        super(binding.getRoot());

        this.binding = binding;
    }

    public static MovieViewHolder create(ViewGroup parent) {
        // Inflate
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Create the binding
        ListItemMovieBinding binding =
                ListItemMovieBinding.inflate(layoutInflater, parent, false);
        return new MovieViewHolder(binding);
    }

    public void bindTo(final MovieDataSet movie, OnItemClickedListener mCallback) {
        binding.setMovie(movie);
        // movie click event
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MovieDetailFragment.MOVIE_DETAIL, movie);
                mCallback.clickedItem(bundle);
            }
        });

        binding.executePendingBindings();
    }
}
