package com.example.searchmoviesomdb.services;

import com.example.searchmoviesomdb.models.DetailsDataSet;
import com.example.searchmoviesomdb.models.MovieDataSet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieSearchService {

    @GET("?type=movie")
    Call<List<MovieDataSet>> getMovieList(@Query("s") String Title, @Query(value = "apikey", encoded = true) String key);

    @GET("?plot=full")
    Call<DetailsDataSet> getMovieDetails(@Query("i") String ImdbId, @Query(value = "apikey", encoded = true) String key);
}
