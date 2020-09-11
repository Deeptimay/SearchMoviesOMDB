package com.example.searchmoviesomdb.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.searchmoviesomdb.R;
import com.example.searchmoviesomdb.callbacks.OnItemClickedListener;
import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.ui.MovieDetailFragment;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {

    Context context;
    OnItemClickedListener mCallback;
    private List<MovieDataSet> mValues;

    public MovieRecyclerViewAdapter(List<MovieDataSet> items, Context context, OnItemClickedListener mCallback) {
        this.mValues = items;
        this.context = context;
        this.mCallback = mCallback;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, final int position) {

        final MovieDataSet detail = mValues.get(position);
        holder.mDirectorView.setText(detail.Type);
        holder.mTitleView.setText(detail.Title);
        holder.mYearView.setText(detail.Year);

        final String imageUrl;
        if (!detail.Poster.equals("N/A")) {
            imageUrl = detail.Poster;
        } else {
            // default image if there is no poster available
            imageUrl = context.getResources().getString(R.string.default_poster);
        }
        holder.mThumbImageView.layout(0, 0, 0, 0); // invalidate the width so that glide wont use that dimension
        Glide.with(context).load(imageUrl).into(holder.mThumbImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putSerializable(MovieDetailFragment.MOVIE_DETAIL, detail);
                mCallback.clickedItem(bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mValues == null) {
            return 0;
        }
        return mValues.size();
    }

    @Override
    public void onViewRecycled(MovieViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(holder.mThumbImageView.getContext()).clear(holder.mThumbImageView);
    }

    public void setData(List<MovieDataSet> data) {
        this.mValues = data;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mYearView;
        public final TextView mDirectorView;
        public final ImageView mThumbImageView;

        public MovieViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.movie_title);
            mYearView = (TextView) view.findViewById(R.id.movie_year);
            mThumbImageView = (ImageView) view.findViewById(R.id.thumbnail);
            mDirectorView = (TextView) view.findViewById(R.id.movie_director);
        }
    }
}