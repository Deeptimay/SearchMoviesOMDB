package com.example.searchmoviesomdb.services;

import android.content.Context;
import android.util.Log;

import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;

public class RetrofitLoader extends AsyncTaskLoader<SearchService.ResultWithDetail> {

    private static final String LOG_TAG = "RetrofitLoader";

    private final String mTitle;

    private SearchService.ResultWithDetail mData;

    public RetrofitLoader(Context context, String title) {
        super(context);
        mTitle = title;
    }

    @Override
    public SearchService.ResultWithDetail loadInBackground() {
        // get results from calling API
        try {
            SearchService.Result result = SearchService.performSearch(mTitle);
            SearchService.ResultWithDetail resultWithDetail = new SearchService.ResultWithDetail(result);
            if (result.Search != null) {
                for (SearchService.Movie movie : result.Search) {
                    resultWithDetail.addToList(SearchService.getDetail(movie.imdbID));
                }
            }
            return resultWithDetail;
        } catch (final IOException e) {
            Log.e(LOG_TAG, "Error from api access", e);
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mData); // makes loaderManager call onLoadFinished
        } else {
            forceLoad(); // calls the loadInBackground
        }
    }


    @Override
    protected void onReset() {
        Log.d(LOG_TAG, "onReset");
        super.onReset();
        mData = null;
    }

    @Override
    public void deliverResult(SearchService.ResultWithDetail data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        SearchService.ResultWithDetail oldData = mData;
        mData = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

    }
}
