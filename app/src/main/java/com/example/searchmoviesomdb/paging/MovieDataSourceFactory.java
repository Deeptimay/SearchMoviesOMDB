package com.example.searchmoviesomdb.paging;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.services.MovieSearchService;

import java.util.concurrent.Executor;

/**
 * A simple data source factory provides a way to observe the last created data source.
 */
public class MovieDataSourceFactory extends DataSource.Factory<Integer, MovieDataSet> {

    private final MovieSearchService movieService;
    private final Executor networkExecutor;
    private final String key;
    public MutableLiveData<MoviePageKeyedDataSource> sourceLiveData = new MutableLiveData<>();

    public MovieDataSourceFactory(MovieSearchService movieService,
                                  Executor networkExecutor, String key) {
        this.movieService = movieService;
        this.key = key;
        this.networkExecutor = networkExecutor;
    }

    @Override
    public DataSource<Integer, MovieDataSet> create() {
        MoviePageKeyedDataSource movieDataSource =
                new MoviePageKeyedDataSource(movieService, networkExecutor, key);
        sourceLiveData.postValue(movieDataSource);
        return movieDataSource;
    }
}
