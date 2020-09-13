package com.example.searchmoviesomdb.data;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.searchmoviesomdb.Utils.AppExecutors;
import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.models.MovieDetailDataSet;
import com.example.searchmoviesomdb.models.RepoMoviesResult;
import com.example.searchmoviesomdb.paging.MovieDataSourceFactory;
import com.example.searchmoviesomdb.services.MovieSearchService;

public class MoviesRemoteDataSource {

    private static final int PAGE_SIZE = 20;
    private static volatile MoviesRemoteDataSource sInstance;
    private final AppExecutors mExecutors;
    private final MovieSearchService mMovieService;

    private MoviesRemoteDataSource(MovieSearchService movieService,
                                   AppExecutors executors) {
        mMovieService = movieService;
        mExecutors = executors;
    }

    public static MoviesRemoteDataSource getInstance(MovieSearchService movieService,
                                                     AppExecutors executors) {
        if (sInstance == null) {
            synchronized (AppExecutors.class) {
                if (sInstance == null) {
                    sInstance = new MoviesRemoteDataSource(movieService, executors);
                }
            }
        }
        return sInstance;
    }

    public LiveData<MovieDetailDataSet> loadMovie(final String movieId) {
        return MoviesDetailsRepository.getInstance().getMovieDetails(movieId);
    }

    /**
     * Load movies for certain filter.
     */
    public RepoMoviesResult loadMoviesFilteredBy(String key) {
        MovieDataSourceFactory sourceFactory =
                new MovieDataSourceFactory(mMovieService, mExecutors.networkIO(), key);

        // paging configuration
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(PAGE_SIZE)
                .build();

        // Get the paged list
        LiveData<PagedList<MovieDataSet>> moviesPagedList = new LivePagedListBuilder<>(sourceFactory, config)
                .setFetchExecutor(mExecutors.networkIO())
                .build();


        // Get pagedList and network errors exposed to the viewmodel
        return new RepoMoviesResult(
                moviesPagedList,
                sourceFactory.sourceLiveData
        );
    }
}
