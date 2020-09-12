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
        ((MovieViewHolder) holder).bindTo(getItem(position));

//        final MovieDataSet detail = mValues.get(position);
//        ((MovieViewHolder) holder).mDirectorView.setText(detail.Type);
//        ((MovieViewHolder) holder).mTitleView.setText(detail.Title);
//        ((MovieViewHolder) holder).mYearView.setText(detail.Year);
//
//        final String imageUrl;
//        if (!detail.Poster.equals("N/A")) {
//            imageUrl = detail.Poster;
//        } else {
//            // default image if there is no poster available
//            imageUrl = context.getResources().getString(R.string.default_poster);
//        }
//        ((MovieViewHolder) holder).mThumbImageView.layout(0, 0, 0, 0); // invalidate the width so that glide wont use that dimension
//        Glide.with(context).load(imageUrl).into(((MovieViewHolder) holder).mThumbImageView);
//
//        ((MovieViewHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(MovieDetailFragment.MOVIE_DETAIL, detail);
//                mCallback.clickedItem(bundle);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (mValues == null) {
            return 0;
        }
        return mValues.size();
    }

//    @Override
//    public void onViewRecycled(RecyclerView.ViewHolder holder) {
//        super.onViewRecycled(holder);
//        Glide.with(((MovieViewHolder) holder).mThumbImageView.getContext()).clear(((MovieViewHolder) holder).mThumbImageView);
//    }

    public void setData(List<MovieDataSet> data) {
        mValues = data;
        notifyDataSetChanged();
    }

//    public class MovieViewHolder extends RecyclerView.ViewHolder {
//        public final View mView;
//        public final TextView mTitleView;
//        public final TextView mYearView;
//        public final TextView mDirectorView;
//        public final ImageView mThumbImageView;
//
//        public MovieViewHolder(View view) {
//            super(view);
//            mView = view;
//            mTitleView = (TextView) view.findViewById(R.id.movie_title);
//            mYearView = (TextView) view.findViewById(R.id.movie_year);
//            mThumbImageView = (ImageView) view.findViewById(R.id.thumbnail);
//            mDirectorView = (TextView) view.findViewById(R.id.movie_director);
//        }
//    }
}