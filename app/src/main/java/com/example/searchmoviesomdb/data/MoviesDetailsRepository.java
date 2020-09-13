package com.example.searchmoviesomdb.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.searchmoviesomdb.models.MovieDetailDataSet;
import com.example.searchmoviesomdb.services.MovieSearchService;
import com.example.searchmoviesomdb.services.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesDetailsRepository {

    private static final String TAG = MoviesDetailsRepository.class.getName();
    static MoviesDetailsRepository moviesDetailsRepository = new MoviesDetailsRepository();
    private MutableLiveData<MovieDetailDataSet> detailsDataSetMutableLiveData = new MutableLiveData<>();

    public MoviesDetailsRepository() {
    }

    public static MoviesDetailsRepository getInstance() {
        if (moviesDetailsRepository == null) {
            moviesDetailsRepository = new MoviesDetailsRepository();
        }
        return moviesDetailsRepository;
    }

    public LiveData<MovieDetailDataSet> getMovieDetails(String imdbId) {
        MovieSearchService service = RetrofitClient.getInstance();
        Call<MovieDetailDataSet> call = service.getMovieDetails(imdbId);

        call.enqueue(new Callback<MovieDetailDataSet>() {
            @Override
            public void onResponse(Call<MovieDetailDataSet> call, Response<MovieDetailDataSet> response) {
                try {
                    if (response.body() != null) {
                        detailsDataSetMutableLiveData.setValue(response.body());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error while parsing search results", e);
                }
            }

            @Override
            public void onFailure(Call<MovieDetailDataSet> call, Throwable t) {
                Log.e(TAG, "Error while fetching search results", t);
            }
        });
        return detailsDataSetMutableLiveData;
    }
}