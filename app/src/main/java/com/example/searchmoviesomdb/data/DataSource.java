package com.example.searchmoviesomdb.data;

import androidx.lifecycle.LiveData;

import com.example.searchmoviesomdb.models.MovieDetailDataSet;
import com.example.searchmoviesomdb.models.RepoMoviesResult;

public interface DataSource {

    LiveData<MovieDetailDataSet> loadMovie(String movieId);

    RepoMoviesResult getAllFavoriteMovies(String key);
}
