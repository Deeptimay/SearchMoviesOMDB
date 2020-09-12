package com.example.searchmoviesomdb.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.searchmoviesomdb.databinding.ListItemMovieBinding;
import com.example.searchmoviesomdb.models.MovieDataSet;


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

    public void bindTo(final MovieDataSet movie) {
        binding.setMovie(movie);
        // movie click event
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
//                intent.putExtra(DetailsActivity.EXTRA_MOVIE_ID, movie.getId());
//                view.getContext().startActivity(intent);
            }
        });

        binding.executePendingBindings();
    }
}
