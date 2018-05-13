package com.amit.myyoutubeapp;

import android.app.Application;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.SearchResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * AppApplication.
 */

public class AppApplication extends Application {

    private static final String[] SCOPES = {YouTubeScopes.YOUTUBE_READONLY};
    public static AppApplication appApplication;
    private GoogleAccountCredential mCredential;
    private List<SearchResult> searchResults;

    public static AppApplication getInstance() {
        if (appApplication != null) {
            return appApplication;
        }
        return new AppApplication();
    }

    public GoogleAccountCredential getCredential() {
        return mCredential;
    }

    public List<SearchResult> getSearchResults() {
        return searchResults;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appApplication = this;
        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
        searchResults = new ArrayList<>();
    }


}
