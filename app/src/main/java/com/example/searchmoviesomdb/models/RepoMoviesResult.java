package com.example.searchmoviesomdb.models;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PagedList;

import com.example.searchmoviesomdb.paging.MoviePageKeyedDataSource;

public class RepoMoviesResult {
    public LiveData<PagedList<MovieDataSet>> data;
    public MutableLiveData<MoviePageKeyedDataSource> sourceLiveData;

    public RepoMoviesResult(LiveData<PagedList<MovieDataSet>> data,
                            MutableLiveData<MoviePageKeyedDataSource> sourceLiveData) {
        this.data = data;
        this.sourceLiveData = sourceLiveData;
    }
}
