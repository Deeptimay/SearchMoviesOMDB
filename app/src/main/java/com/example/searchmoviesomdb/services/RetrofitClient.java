package com.example.searchmoviesomdb.services;

import com.example.searchmoviesomdb.MyApplication;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String API_URL = "http://www.omdbapi.com";
    static int cacheSize = 10 * 1024 * 1024; // 10 MB
    static Cache cache = new Cache(MyApplication.getInstance().getCacheDir(), cacheSize);
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit != null)
            return retrofit;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public okhttp3.Response intercept(Chain chain) throws IOException {
//                Request request = chain.request().newBuilder()
//                        .addHeader("id", Utils.getUserId(Application.getContext()))
//                        .addHeader("token", Utils.getToken(Application.getContext()))
//                        .build();
//                return chain.proceed(request);
//            }
//        });

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor);

        OkHttpClient okHttpClient = httpClient.cache(cache).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }
}
