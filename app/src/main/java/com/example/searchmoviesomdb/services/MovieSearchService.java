package com.example.searchmoviesomdb.services;

import com.example.searchmoviesomdb.models.MovieDetailDataSet;
import com.example.searchmoviesomdb.models.SearchDataSet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieSearchService {

    @GET("?type=movie")
    Call<SearchDataSet> getMovieList(@Query("s") String Title, @Query("page") String count);

    @GET("?plot=full")
    Call<MovieDetailDataSet> getMovieDetails(@Query("i") String ImdbId);
}
