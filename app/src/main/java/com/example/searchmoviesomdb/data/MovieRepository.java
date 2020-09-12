package com.example.searchmoviesomdb.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.searchmoviesomdb.Utils.AppExecutors;
import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.models.MovieDetailDataSet;
import com.example.searchmoviesomdb.models.RepoMoviesResult;

import java.util.List;


/**
 * Repository implementation that returns a paginated data and loads data directly from network.
 */
public class MovieRepository implements DataSource {

    private static volatile MovieRepository sInstance;

    private final MoviesRemoteDataSource mRemoteDataSource;

    private final AppExecutors mExecutors;

    private MovieRepository(MoviesRemoteDataSource remoteDataSource,
                            AppExecutors executors) {
        mRemoteDataSource = remoteDataSource;
        mExecutors = executors;
    }

    public static MovieRepository getInstance(MoviesRemoteDataSource remoteDataSource,
                                              AppExecutors executors) {
        if (sInstance == null) {
            synchronized (MovieRepository.class) {
                if (sInstance == null) {
                    sInstance = new MovieRepository(remoteDataSource, executors);
                }
            }
        }
        return sInstance;
    }

    @Override
    public LiveData<MovieDetailDataSet> loadMovie(String movieId) {
        return  mRemoteDataSource.loadMovie(movieId);
    }

    @Override
    public RepoMoviesResult getAllFavoriteMovies(String key) {
        return  mRemoteDataSource.loadMoviesFilteredBy(key);
    }
}
