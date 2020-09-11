package com.example.searchmoviesomdb.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.searchmoviesomdb.R;
import com.example.searchmoviesomdb.Utils.CommonUtils;
import com.example.searchmoviesomdb.adapter.MovieRecyclerViewAdapter;
import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.viewmodels.MoviesViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class FirstFragment extends Fragment implements MovieRecyclerViewAdapter.OnItemClickedListener {

    private static final String LOG_TAG = "FirstFragment";
    ProgressDialog progressDialog;
    SearchView searchView;
    MoviesViewModel moviesViewModel;
    View view;
    private RecyclerView mMovieListRecyclerView;
    private MovieRecyclerViewAdapter mMovieAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first, container, false);
        initView();
        return view;
    }

    private void initView() {
        searchView = view.findViewById(R.id.search);
        mMovieListRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        setupSearch();
        setRecyclerView();
        moviesViewModel = ViewModelProviders.of(FirstFragment.this.getActivity()).get(MoviesViewModel.class);
    }

    private void setRecyclerView() {
        List<MovieDataSet> movieDataSets = new ArrayList<>();
        mMovieAdapter = new MovieRecyclerViewAdapter(movieDataSets, FirstFragment.this.getActivity());
        mMovieListRecyclerView.setAdapter(mMovieAdapter);
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_column_count), StaggeredGridLayoutManager.VERTICAL);
        mMovieListRecyclerView.setItemAnimator(null);
        mMovieListRecyclerView.setLayoutManager(gridLayoutManager);
    }

    public void setupSearch() {

        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return true;
            }
        });
    }

    private void getAllSearchResult(String query) {
        progressDialog = new ProgressDialog(FirstFragment.this.getActivity());
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        moviesViewModel.searchMovies(query).observe(getViewLifecycleOwner(), new Observer<List<MovieDataSet>>() {
            @Override
            public void onChanged(@Nullable List<MovieDataSet> movieDataSetList) {
                if (movieDataSetList != null)
                    mMovieAdapter.setData(movieDataSetList);
                progressDialog.dismiss();
            }
        });
    }

    private void startSearch(String query) {
        if (CommonUtils.isNetworkAvailable(getActivity().getApplicationContext())) {
            CommonUtils.hideSoftKeyboard(getActivity());
            if (!query.isEmpty()) {
                getAllSearchResult(query);
            } else
                Snackbar.make(view.findViewById(R.id.container),
                        getResources().getString(R.string.snackbar_title_empty),
                        Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(view.findViewById(R.id.container),
                    getResources().getString(R.string.network_not_available),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void clickedItem(Bundle data) {
        NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, data);
    }
}