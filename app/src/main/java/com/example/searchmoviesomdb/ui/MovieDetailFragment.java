package com.example.searchmoviesomdb.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.searchmoviesomdb.R;
import com.example.searchmoviesomdb.Utils.CommonUtils;
import com.example.searchmoviesomdb.Utils.Injection;
import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.models.MovieDetailDataSet;
import com.example.searchmoviesomdb.viewmodels.MovieDetailsViewModel;
import com.example.searchmoviesomdb.viewmodels.MoviesViewModel;
import com.example.searchmoviesomdb.viewmodels.ViewModelFactory;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

public class MovieDetailFragment extends Fragment {

    public static final String MOVIE_DETAIL = "movie_detail";
    MoviesViewModel moviesViewModel;
    ProgressDialog progressDialog;
    View view;

    private MovieDetailsViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_second, container, false);
        initView();
        setupToolbar();
        return view;
    }

    private void initView() {

        MovieDataSet movieDataSet = (MovieDataSet) getArguments().getSerializable(MOVIE_DETAIL);

        if (CommonUtils.isNetworkAvailable(getActivity().getApplicationContext())) {
            mViewModel = obtainViewModel();
            mViewModel.init(movieDataSet.imdbID);

            mViewModel.getResult().observe(getViewLifecycleOwner(), new Observer<MovieDetailDataSet>() {
                @Override
                public void onChanged(MovieDetailDataSet resource) {
                    inflateData(resource);
                }
            });
        } else
            Snackbar.make(view.findViewById(R.id.container),
                    getResources().getString(R.string.network_not_available),
                    Snackbar.LENGTH_LONG).show();
        Glide.with(this).load(movieDataSet.Poster).into((ImageView) view.findViewById(R.id.main_backdrop));
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setTitle(movieDataSet.Title);
    }

    private MovieDetailsViewModel obtainViewModel() {
        ViewModelFactory factory = Injection.provideViewModelFactory(MovieDetailFragment.this.getActivity());
        return ViewModelProviders.of(this, factory).get(MovieDetailsViewModel.class);
    }

    private void getMovieDetails(String query) {
        progressDialog = new ProgressDialog(MovieDetailFragment.this.getActivity());
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        moviesViewModel.getMovieDetails(query).observe(getViewLifecycleOwner(), new Observer<MovieDetailDataSet>() {
            @Override
            public void onChanged(@Nullable MovieDetailDataSet movieDataSetList) {
                if (movieDataSetList != null)
                    inflateData(movieDataSetList);
                progressDialog.dismiss();
            }
        });
    }

    private void inflateData(MovieDetailDataSet movieDataSetList) {
        ((TextView) view.findViewById(R.id.grid_title)).setText(movieDataSetList.Title);
        ((TextView) view.findViewById(R.id.grid_writers)).setText(movieDataSetList.Writer);
        ((TextView) view.findViewById(R.id.grid_actors)).setText(movieDataSetList.Actors);
        ((TextView) view.findViewById(R.id.grid_director)).setText(movieDataSetList.Director);
        ((TextView) view.findViewById(R.id.grid_genre)).setText(movieDataSetList.Genre);
        ((TextView) view.findViewById(R.id.grid_released)).setText(movieDataSetList.Released);
        ((TextView) view.findViewById(R.id.grid_plot)).setText(movieDataSetList.Plot);
        ((TextView) view.findViewById(R.id.grid_runtime)).setText(movieDataSetList.Runtime);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}