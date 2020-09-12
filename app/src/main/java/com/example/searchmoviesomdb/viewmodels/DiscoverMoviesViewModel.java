package com.example.searchmoviesomdb.viewmodels;


import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.searchmoviesomdb.data.MovieRepository;
import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.models.RepoMoviesResult;


public class DiscoverMoviesViewModel extends ViewModel {

    private LiveData<RepoMoviesResult> repoMoviesResult;

    private LiveData<PagedList<MovieDataSet>> pagedList;

    private MutableLiveData<String> key = new MutableLiveData<>();

    public DiscoverMoviesViewModel(final MovieRepository movieRepository) {
        // By default show popular movies
        key.setValue("Star Trek");

        repoMoviesResult = Transformations.map(key, new Function<String, RepoMoviesResult>() {
            @Override
            public RepoMoviesResult apply(String sort) {
                return movieRepository.getAllFavoriteMovies(sort);
            }
        });
        pagedList = Transformations.switchMap(repoMoviesResult,
                new Function<RepoMoviesResult, LiveData<PagedList<MovieDataSet>>>() {
                    @Override
                    public LiveData<PagedList<MovieDataSet>> apply(RepoMoviesResult input) {
                        return input.data;
                    }
                });
    }

    public LiveData<PagedList<MovieDataSet>> getPagedList() {
        return pagedList;
    }

    public String getCurrentKey() {
        return key.getValue();
    }

    public void setCurrentKey(String SearchKey) {
        key.setValue(SearchKey);
    }

    // retry any failed requests.
    public void retry() {
        repoMoviesResult.getValue().sourceLiveData.getValue().retryCallback.invoke();
    }
}