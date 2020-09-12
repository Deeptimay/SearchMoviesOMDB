package com.example.searchmoviesomdb.viewmodels;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.searchmoviesomdb.data.MovieRepository;
import com.example.searchmoviesomdb.models.MovieDetailDataSet;


public class MovieDetailsViewModel extends ViewModel {

    private final MovieRepository repository;

    private LiveData<MovieDetailDataSet> result;

    private MutableLiveData<String> movieIdLiveData = new MutableLiveData<>();


    public MovieDetailsViewModel(final MovieRepository repository) {
        this.repository = repository;
    }

    public void init(String movieId) {
        if (result != null) {
            return; // load movie details only once the activity created first time
        }

        result = Transformations.switchMap(movieIdLiveData,
                new Function<String, LiveData<MovieDetailDataSet>>() {
                    @Override
                    public LiveData<MovieDetailDataSet> apply(String movieId) {
                        return repository.loadMovie(movieId);
                    }
                });

        setMovieIdLiveData(movieId); // trigger loading movie
    }

    public LiveData<MovieDetailDataSet> getResult() {
        return result;
    }

    private void setMovieIdLiveData(String movieId) {
        movieIdLiveData.setValue(movieId);
    }

    public void retry(String movieId) {
        setMovieIdLiveData(movieId);
    }

}
