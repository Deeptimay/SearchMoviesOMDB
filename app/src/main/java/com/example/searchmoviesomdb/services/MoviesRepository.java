package com.example.searchmoviesomdb.services;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.searchmoviesomdb.BuildConfig;
import com.example.searchmoviesomdb.models.DetailsDataSet;
import com.example.searchmoviesomdb.models.MovieDataSet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesRepository {

    private static final String TAG = MoviesRepository.class.getName();
    static MoviesRepository moviesRepository = new MoviesRepository();
    private MutableLiveData<List<MovieDataSet>> movieDataSetMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<DetailsDataSet> detailsDataSetMutableLiveData = new MutableLiveData<>();

    public MoviesRepository() {
        movieListSearch("");
    }

    public static MoviesRepository getInstance() {
        if (moviesRepository == null) {
            moviesRepository = new MoviesRepository();
        }
        return moviesRepository;
    }

    private void setMovieList(List<MovieDataSet> movieDataSetList) {
        movieDataSetMutableLiveData.postValue(movieDataSetList);
    }

    private void setMovieDetails(DetailsDataSet detailsDataSet) {
        detailsDataSetMutableLiveData.postValue(detailsDataSet);
    }

    public LiveData<List<MovieDataSet>> getMovieList(String query) {
        movieListSearch(query);
        return movieDataSetMutableLiveData;
    }

    public LiveData<DetailsDataSet> getMovieDetails(String imdbId) {
        movieDetailsSearch(imdbId);
        return detailsDataSetMutableLiveData;
    }

    private void movieListSearch(String query) {
        MovieSearchService service = RetrofitClient.getClient().create(MovieSearchService.class);
        Call<List<MovieDataSet>> call = service.getMovieList(query, BuildConfig.API_KEY);

        call.enqueue(new Callback<List<MovieDataSet>>() {
            @Override
            public void onResponse(Call<List<MovieDataSet>> call, Response<List<MovieDataSet>> response) {
                try {
                    if (response.body() != null) {
                        setMovieList(response.body());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error while parsing search results", e);
                }
            }

            @Override
            public void onFailure(Call<List<MovieDataSet>> call, Throwable t) {
                Log.e(TAG, "Error while fetching search results", t);
            }
        });
    }

    private void movieDetailsSearch(String imdbId) {
        MovieSearchService service = RetrofitClient.getClient().create(MovieSearchService.class);
        Call<DetailsDataSet> call = service.getMovieDetails(imdbId, BuildConfig.API_KEY);

        call.enqueue(new Callback<DetailsDataSet>() {
            @Override
            public void onResponse(Call<DetailsDataSet> call, Response<DetailsDataSet> response) {
                try {
                    if (response.body() != null) {
                        setMovieDetails(response.body());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error while parsing search results", e);
                }
            }

            @Override
            public void onFailure(Call<DetailsDataSet> call, Throwable t) {
                Log.e(TAG, "Error while fetching search results", t);
            }
        });
    }
}