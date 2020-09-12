package com.example.searchmoviesomdb.Utils;

import android.content.Context;

import com.example.searchmoviesomdb.data.MovieRepository;
import com.example.searchmoviesomdb.data.MoviesRemoteDataSource;
import com.example.searchmoviesomdb.services.MovieSearchService;
import com.example.searchmoviesomdb.services.RetrofitClient;
import com.example.searchmoviesomdb.viewmodels.ViewModelFactory;


public class Injection {

    /**
     * Creates an instance of MoviesRemoteDataSource
     */
    public static MoviesRemoteDataSource provideMoviesRemoteDataSource() {
        MovieSearchService apiService = RetrofitClient.getInstance();
        AppExecutors executors = AppExecutors.getInstance();
        return MoviesRemoteDataSource.getInstance(apiService, executors);
    }

    /**
     * Creates an instance of MovieRepository
     */
    public static MovieRepository provideMovieRepository(Context context) {
        MoviesRemoteDataSource remoteDataSource = provideMoviesRemoteDataSource();
        AppExecutors executors = AppExecutors.getInstance();
        return MovieRepository.getInstance(
                remoteDataSource,
                executors);
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        MovieRepository repository = provideMovieRepository(context);
        return ViewModelFactory.getInstance(repository);
    }
}
