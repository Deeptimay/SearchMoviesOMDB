package com.example.searchmoviesomdb.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.searchmoviesomdb.R;
import com.example.searchmoviesomdb.Utils.CommonUtils;
import com.example.searchmoviesomdb.Utils.Injection;
import com.example.searchmoviesomdb.adapter.MovieRecyclerViewAdapter;
import com.example.searchmoviesomdb.callbacks.OnItemClickedListener;
import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.viewmodels.DiscoverMoviesViewModel;
import com.example.searchmoviesomdb.viewmodels.ViewModelFactory;
import com.google.android.material.snackbar.Snackbar;


public class MovieListFragment extends Fragment implements OnItemClickedListener {

    private static final String LOG_TAG = MovieListFragment.class.getCanonicalName();
    SearchView searchView;
    View view;
    private RecyclerView mMovieListRecyclerView;
    private MovieRecyclerViewAdapter mMovieAdapter;
    private ProgressBar mProgressBar;
    private DiscoverMoviesViewModel viewModel;

    public static DiscoverMoviesViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = Injection.provideViewModelFactory(activity);
        return ViewModelProviders.of(activity, factory).get(DiscoverMoviesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = obtainViewModel(getActivity());
        initView();
    }

    private void initView() {
        searchView = view.findViewById(R.id.search);
        mMovieListRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_spinner);

        setupSearch();
        setRecyclerView();
    }

    private void setRecyclerView() {
        // First param is number of columns and second param is orientation i.e Vertical or Horizontal
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_column_count), StaggeredGridLayoutManager.VERTICAL);
        mMovieListRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mMovieListRecyclerView.setLayoutManager(gridLayoutManager);
        mMovieAdapter = new MovieRecyclerViewAdapter(viewModel, MovieListFragment.this.getActivity(), MovieListFragment.this);
        mMovieListRecyclerView.setAdapter(mMovieAdapter);

        // observe paged list
        viewModel.getPagedList().observe(getViewLifecycleOwner(), new Observer<PagedList<MovieDataSet>>() {
            @Override
            public void onChanged(PagedList<MovieDataSet> movies) {
                mMovieAdapter.submitList(movies);
                mProgressBar.setVisibility(View.GONE);
                mMovieListRecyclerView.setVisibility(View.VISIBLE);
            }
        });
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
                if (!query.isEmpty())
                    startSearch(query);
                return true;
            }
        });
    }

    private void startSearch(String query) {
        if (!query.isEmpty()) {
            mProgressBar.setVisibility(View.VISIBLE);
            mMovieListRecyclerView.setVisibility(View.INVISIBLE);
            if (!viewModel.getCurrentKey().equalsIgnoreCase(query))
                viewModel.setCurrentKey(query);
        } else
            Snackbar.make(view.findViewById(R.id.container),
                    getResources().getString(R.string.snackbar_title_empty),
                    Snackbar.LENGTH_LONG).show();

        if (!CommonUtils.isNetworkAvailable(getActivity().getApplicationContext()))
            Snackbar.make(view.findViewById(R.id.container),
                    getResources().getString(R.string.network_not_available),
                    Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void clickedItem(Bundle data) {
        CommonUtils.hideSoftKeyboard(getActivity());
        NavHostFragment.findNavController(MovieListFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment, data);
    }
}