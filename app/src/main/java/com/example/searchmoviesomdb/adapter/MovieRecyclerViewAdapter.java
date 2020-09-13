package com.example.searchmoviesomdb.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.searchmoviesomdb.callbacks.OnItemClickedListener;
import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.viewmodels.DiscoverMoviesViewModel;

import java.util.List;

public class MovieRecyclerViewAdapter extends PagedListAdapter<MovieDataSet, RecyclerView.ViewHolder> {

    private static DiffUtil.ItemCallback<MovieDataSet> MOVIE_COMPARATOR = new DiffUtil.ItemCallback<MovieDataSet>() {
        @Override
        public boolean areItemsTheSame(@NonNull MovieDataSet oldItem, @NonNull MovieDataSet newItem) {
            return oldItem.getImdbID().equals(newItem.getImdbID());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MovieDataSet oldItem, @NonNull MovieDataSet newItem) {
            return oldItem.equals(newItem);
        }
    };

    Context context;
    OnItemClickedListener mCallback;
    DiscoverMoviesViewModel viewModel;
    private List<MovieDataSet> mValues;

    public MovieRecyclerViewAdapter(DiscoverMoviesViewModel viewModel, Context context, OnItemClickedListener mCallback) {
        super(MOVIE_COMPARATOR);
        this.viewModel = viewModel;
        this.context = context;
        this.mCallback = mCallback;
        mValues = viewModel.getPagedList().getValue();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MovieViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MovieViewHolder) holder).bindTo(getItem(position), mCallback);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}