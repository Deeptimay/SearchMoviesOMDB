package com.example.searchmoviesomdb.services;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.searchmoviesomdb.BuildConfig;
import com.example.searchmoviesomdb.models.MovieDetailDataSet;
import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.models.SearchDataSet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesRepository {

    private static final String TAG = MoviesRepository.class.getName();
    static MoviesRepository moviesRepository = new MoviesRepository();
    private MutableLiveData<List<MovieDataSet>> movieDataSetMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<MovieDetailDataSet> detailsDataSetMutableLiveData = new MutableLiveData<>();

    public MoviesRepository() {
    }

    public static MoviesRepository getInstance() {
        if (moviesRepository == null) {
            moviesRepository = new MoviesRepository();
        }
        return moviesRepository;
    }

    public LiveData<List<MovieDataSet>> getMovieList(String query) {
        MovieSearchService service = RetrofitClient.getClient().create(MovieSearchService.class);
        Call<SearchDataSet> call = service.getMovieList(query, BuildConfig.API_KEY);

        call.enqueue(new Callback<SearchDataSet>() {
            @Override
            public void onResponse(Call<SearchDataSet> call, Response<SearchDataSet> response) {
                try {
                    if (response.body() != null) {
                        movieDataSetMutableLiveData.setValue(response.body().getSearch());
                    }
                } catch (Exception e) {
                    movieDataSetMutableLiveData.setValue(null);
                    Log.e(TAG, "Error while parsing search results", e);
                }
            }

            @Override
            public void onFailure(Call<SearchDataSet> call, Throwable t) {
                Log.e(TAG, "Error while fetching search results", t);
                movieDataSetMutableLiveData.setValue(null);
            }
        });
        return movieDataSetMutableLiveData;
    }

    public LiveData<MovieDetailDataSet> getMovieDetails(String imdbId) {
        MovieSearchService service = RetrofitClient.getClient().create(MovieSearchService.class);
        Call<MovieDetailDataSet> call = service.getMovieDetails(imdbId, BuildConfig.API_KEY);

        call.enqueue(new Callback<MovieDetailDataSet>() {
            @Override
            public void onResponse(Call<MovieDetailDataSet> call, Response<MovieDetailDataSet> response) {
                try {
                    if (response.body() != null) {
                        detailsDataSetMutableLiveData.setValue(response.body());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error while parsing search results", e);
                    detailsDataSetMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieDetailDataSet> call, Throwable t) {
                Log.e(TAG, "Error while fetching search results", t);
                detailsDataSetMutableLiveData.setValue(null);
            }
        });

        return detailsDataSetMutableLiveData;
    }
}