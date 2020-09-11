package com.example.searchmoviesomdb.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.searchmoviesomdb.R;
import com.example.searchmoviesomdb.Utils.CommonUtils;
import com.example.searchmoviesomdb.models.DetailsDataSet;
import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.viewmodels.MoviesViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;

public class SecondFragment extends Fragment {

    public static final String MOVIE_DETAIL = "movie_detail";
    MoviesViewModel moviesViewModel;
    ProgressDialog progressDialog;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_second, container, false);
        initView();
        return view;
    }

    private void initView() {
        moviesViewModel = ViewModelProviders.of(SecondFragment.this.getActivity()).get(MoviesViewModel.class);
        MovieDataSet movieDataSet = getArguments().getParcelable(MOVIE_DETAIL);
        if (CommonUtils.isNetworkAvailable(getActivity().getApplicationContext()))
            getMovieDetails(movieDataSet.imdbID);
        else
            Snackbar.make(view.findViewById(R.id.container),
                    getResources().getString(R.string.network_not_available),
                    Snackbar.LENGTH_LONG).show();
        Glide.with(this).load(movieDataSet.imdbID).into((ImageView) view.findViewById(R.id.main_backdrop));
    }

    private void getMovieDetails(String query) {
        progressDialog = new ProgressDialog(SecondFragment.this.getActivity());
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        moviesViewModel.getMovieDetails(query).observe(getViewLifecycleOwner(), new Observer<DetailsDataSet>() {
            @Override
            public void onChanged(@Nullable DetailsDataSet movieDataSetList) {
                if (movieDataSetList != null)
                    inflateData(movieDataSetList);
                progressDialog.dismiss();
            }
        });
    }

    private void inflateData(DetailsDataSet movieDataSetList) {

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setTitle(movieDataSetList.Title);

        ((TextView) view.findViewById(R.id.grid_title)).setText(movieDataSetList.Title);
        ((TextView) view.findViewById(R.id.grid_writers)).setText(movieDataSetList.Writer);
        ((TextView) view.findViewById(R.id.grid_actors)).setText(movieDataSetList.Actors);
        ((TextView) view.findViewById(R.id.grid_director)).setText(movieDataSetList.Director);
        ((TextView) view.findViewById(R.id.grid_genre)).setText(movieDataSetList.Genre);
        ((TextView) view.findViewById(R.id.grid_released)).setText(movieDataSetList.Released);
        ((TextView) view.findViewById(R.id.grid_plot)).setText(movieDataSetList.Plot);
        ((TextView) view.findViewById(R.id.grid_runtime)).setText(movieDataSetList.Runtime);
    }
}