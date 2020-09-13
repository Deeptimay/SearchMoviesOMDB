package com.example.searchmoviesomdb.services;

import com.example.searchmoviesomdb.MyApplication;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String API_URL = "http://www.omdbapi.com";
    private static final Object sLock = new Object();
    private static final OkHttpClient client;
    static int cacheSize = 10 * 1024 * 1024; // 10 MB
    static Cache cache = new Cache(MyApplication.getInstance().getCacheDir(), cacheSize);
    private static MovieSearchService sInstance;

    static {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .addInterceptor(logging)
                .cache(cache)
                .build();
    }

    public static MovieSearchService getInstance() {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = getRetrofitInstance().create(MovieSearchService.class);
            }
            return sInstance;
        }
    }

    public static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}
