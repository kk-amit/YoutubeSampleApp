package com.amit.myyoutubeapp.searchmodules;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

import java.io.IOException;

/**
 * APIAsynTask.
 */

public class APIAsynTask extends AsyncTask<String, Void, SearchResponseData> {
    private com.google.api.services.youtube.YouTube mService = null;
    private Exception mLastError = null;
    private IAPIListener listener;

    APIAsynTask(GoogleAccountCredential credential, IAPIListener listener) {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.youtube.YouTube.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("YouTube Data API Android Quickstart")
                .build();
        this.listener = listener;
    }

    /**
     * Background task to call YouTube Data API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected SearchResponseData doInBackground(String... params) {
        try {
            return getDataFromApi(params[0]);
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Fetch information about the "GoogleDevelopers" YouTube channel.
     *
     * @return List of Strings containing information about the channel.
     * @throws IOException
     */
    private SearchResponseData getDataFromApi(String... query) throws IOException {
        SearchResponseData searchResponseData = new SearchResponseData();
        SearchListResponse response = null;
        try {
            YouTube.Search.List searchListByKeywordRequest = mService.search().list("snippet");
            searchListByKeywordRequest.setMaxResults(Long.parseLong("25"));
            searchListByKeywordRequest.setQ(query[0]);
            if (query.length > 1) {
                searchListByKeywordRequest.setPageToken(query[1]);
            }
            searchListByKeywordRequest.setType("video");
            response = searchListByKeywordRequest.execute();
            System.out.println(response);
        } catch (Exception e) {
            searchResponseData.setException(e);
            listener.onCanceled(e);
        }
        searchResponseData.setSearchListResponse(response);
        return searchResponseData;
    }


    @Override
    protected void onPreExecute() {
        if (listener != null) {
            listener.showProgress();
        }
    }

    @Override
    protected void onPostExecute(SearchResponseData output) {
        if (listener != null) {
            listener.hideProgress();
            listener.onCompleted(output);
        }
    }

    @Override
    protected void onCancelled() {
        if (listener != null) {
            listener.hideProgress();
            listener.onCanceled(mLastError);
        }
    }

    void removeListener() {
        listener = null;
    }
}
