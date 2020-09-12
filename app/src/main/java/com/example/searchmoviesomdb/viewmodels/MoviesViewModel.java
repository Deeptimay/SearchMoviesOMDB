package com.example.searchmoviesomdb.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.searchmoviesomdb.models.MovieDetailDataSet;
import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.data.MoviesRepository;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private static final String TAG = MoviesViewModel.class.getName();
    Application application;
    private MoviesRepository moviesRepository;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        moviesRepository = MoviesRepository.getInstance();
    }

    public LiveData<List<MovieDataSet>> searchMovies(String query, String pageCount) {
        return moviesRepository.getMovieList(query, pageCount);
    }

    public LiveData<MovieDetailDataSet> getMovieDetails(String imdbId) {
        return moviesRepository.getMovieDetails(imdbId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }
}