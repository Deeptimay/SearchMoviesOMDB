package com.example.searchmoviesomdb.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.searchmoviesomdb.R;
import com.example.searchmoviesomdb.Utils.CommonUtils;
import com.example.searchmoviesomdb.Utils.EndlessRecyclerViewScrollListener;
import com.example.searchmoviesomdb.adapter.MovieRecyclerViewAdapter;
import com.example.searchmoviesomdb.callbacks.OnItemClickedListener;
import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.viewmodels.MoviesViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class MovieListFragment extends Fragment implements OnItemClickedListener {

    private static final String LOG_TAG = "FirstFragment";
    SearchView searchView;
    MoviesViewModel moviesViewModel;
    View view;
    List<MovieDataSet> movieDataSets = new ArrayList<>();
    int currentPage = 1, totalItemsCount = 10;
    String query;
    private RecyclerView mMovieListRecyclerView;
    private MovieRecyclerViewAdapter mMovieAdapter;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first, container, false);
        initView();
        return view;
    }

    private void initView() {
        searchView = view.findViewById(R.id.search);
        mMovieListRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_spinner);

        moviesViewModel = ViewModelProviders.of(MovieListFragment.this.getActivity()).get(MoviesViewModel.class);

        setupSearch();
        setRecyclerView();
    }

    private void setRecyclerView() {
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_column_count), StaggeredGridLayoutManager.VERTICAL);
        mMovieListRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mMovieListRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                currentPage = page;
                getAllSearchResult();
            }
        });
        mMovieListRecyclerView.setLayoutManager(gridLayoutManager);
        mMovieAdapter = new MovieRecyclerViewAdapter(movieDataSets, MovieListFragment.this.getActivity(), MovieListFragment.this);
        mMovieListRecyclerView.setAdapter(mMovieAdapter);
        startSearch("Star Trek");
        startSearch("Star Trek");
    }

    public void setupSearch() {
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 2)
                    startSearch(query);
                return true;
            }
        });
    }

    private void getAllSearchResult() {
        mProgressBar.setVisibility(View.VISIBLE);
        mMovieListRecyclerView.setVisibility(View.INVISIBLE);
        moviesViewModel.searchMovies(query, String.valueOf(currentPage)).observe(getViewLifecycleOwner(), new Observer<List<MovieDataSet>>() {
            @Override
            public void onChanged(@Nullable List<MovieDataSet> movieDataSetList) {

                mProgressBar.setVisibility(View.GONE);
                mMovieListRecyclerView.setVisibility(View.VISIBLE);
                if (movieDataSetList != null) {
                    movieDataSets.addAll(movieDataSetList);
                    totalItemsCount += movieDataSetList.size();
                }
                mMovieAdapter.setData(movieDataSets);
            }
        });
    }

    private void startSearch(String query_) {
        if (CommonUtils.isNetworkAvailable(getActivity().getApplicationContext())) {
            if (!query_.isEmpty()) {
                query = query_;
                EndlessRecyclerViewScrollListener.resetState();
                movieDataSets.clear();
                currentPage = 1;
                totalItemsCount = 0;
                getAllSearchResult();
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
        CommonUtils.hideSoftKeyboard(getActivity());
        NavHostFragment.findNavController(MovieListFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, data);
    }
}