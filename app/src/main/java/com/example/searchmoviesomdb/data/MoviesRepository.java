package com.example.searchmoviesomdb.data;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.models.MovieDetailDataSet;
import com.example.searchmoviesomdb.models.SearchDataSet;
import com.example.searchmoviesomdb.services.MovieSearchService;
import com.example.searchmoviesomdb.services.RetrofitClient;

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

    public LiveData<List<MovieDataSet>> getMovieList(String query, String pageCount) {
        MovieSearchService service = RetrofitClient.getInstance();
        Call<SearchDataSet> call = service.getMovieList(query, pageCount);

        call.enqueue(new Callback<SearchDataSet>() {
            @Override
            public void onResponse(Call<SearchDataSet> call, Response<SearchDataSet> response) {
                try {
                    if (response.body() != null && response.body().getSearch() != null) {
                        movieDataSetMutableLiveData.setValue(response.body().getSearch());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error while parsing search results", e);
                }
            }

            @Override
            public void onFailure(Call<SearchDataSet> call, Throwable t) {
                Log.e(TAG, "Error while fetching search results", t);
            }
        });
        return movieDataSetMutableLiveData;
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