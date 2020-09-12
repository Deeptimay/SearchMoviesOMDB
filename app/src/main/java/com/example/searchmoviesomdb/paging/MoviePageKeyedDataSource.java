package com.example.searchmoviesomdb.paging;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.searchmoviesomdb.models.MovieDataSet;
import com.example.searchmoviesomdb.models.SearchDataSet;
import com.example.searchmoviesomdb.services.MovieSearchService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A data source that uses the before/after keys returned in page requests.
 * <p>
 */
public class MoviePageKeyedDataSource extends PageKeyedDataSource<Integer, MovieDataSet> {

    private static final int FIRST_PAGE = 1;
    private final MovieSearchService movieService;
    private final Executor networkExecutor;
    private final String key;
    public MutableLiveData<MovieDataSet> networkState = new MutableLiveData<>();
    public RetryCallback retryCallback = null;

    public MoviePageKeyedDataSource(MovieSearchService movieService,
                                    Executor networkExecutor, String key) {
        this.movieService = movieService;
        this.networkExecutor = networkExecutor;
        this.key = key;
    }

    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Integer> params,
                            @NonNull final LoadInitialCallback<Integer, MovieDataSet> callback) {

        // load data from API
        Call<SearchDataSet> request = movieService.getMovieList(key, String.valueOf(FIRST_PAGE));

        // we execute sync since this is triggered by refresh
        try {
            Response<SearchDataSet> response = request.execute();
            SearchDataSet data = response.body();
            List<MovieDataSet> movieList =
                    data != null ? data.getSearch() : Collections.<MovieDataSet>emptyList();

            retryCallback = null;
            callback.onResult(movieList, null, FIRST_PAGE + 1);
        } catch (IOException e) {
            // publish error
            retryCallback = new RetryCallback() {
                @Override
                public void invoke() {
                    networkExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            loadInitial(params, callback);
                        }
                    });

                }
            };
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<Integer, MovieDataSet> callback) {
        // ignored, since we only ever append to our initial load
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params,
                          @NonNull final LoadCallback<Integer, MovieDataSet> callback) {

        Call<SearchDataSet> request = movieService.getMovieList(key, String.valueOf(params.key));

        request.enqueue(new Callback<SearchDataSet>() {
            @Override
            public void onResponse(Call<SearchDataSet> call, Response<SearchDataSet> response) {
                if (response.isSuccessful()) {
                    SearchDataSet data = response.body();
                    List<MovieDataSet> movieList =
                            data != null ? data.getSearch() : Collections.<MovieDataSet>emptyList();

                    retryCallback = null;
                    callback.onResult(movieList, params.key + 1);
                } else {
                    retryCallback = new RetryCallback() {
                        @Override
                        public void invoke() {
                            loadAfter(params, callback);
                        }
                    };
                }
            }

            @Override
            public void onFailure(Call<SearchDataSet> call, Throwable t) {
                retryCallback = new RetryCallback() {
                    @Override
                    public void invoke() {
                        networkExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                loadAfter(params, callback);
                            }
                        });
                    }
                };
            }
        });
    }

    public interface RetryCallback {
        void invoke();
    }
}
