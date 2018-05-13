package com.amit.myyoutubeapp.searchmodules;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

/**
 * SearchPresenter
 */

public class SearchPresenter implements SearchContract.ISearchPresenter, IAPIListener {
    private SearchContract.ISearchView searchView;

    private APIAsynTask apiAsynTask;

    SearchPresenter() {
    }

    @Override
    public void addView(SearchContract.ISearchView searchView) {
        this.searchView = searchView;
    }

    @Override
    public void removeView() {
        if (apiAsynTask != null) {
            apiAsynTask.removeListener();
        }
        searchView = null;

    }

    @Override
    public void performSearch(GoogleAccountCredential credential, String... query) {
        apiAsynTask = new APIAsynTask(credential, this);
        apiAsynTask.execute(query[0], query[1]);
    }


    @Override
    public void showProgress() {
        if (searchView != null) {
            searchView.showProgress();
        }
    }

    @Override
    public void hideProgress() {
        if (searchView != null) {
            searchView.hideProgress();
        }
    }

    @Override
    public void onCompleted(SearchResponseData output) {
        if (searchView != null) {
            searchView.hideProgress();
            if (output.getSearchListResponse() != null) {
                searchView.loadData(output.getSearchListResponse());
            } else {
                if (output.getException() != null)
                    searchView.updateText(output.getException().getCause().toString());
            }
        }
    }

    @Override
    public void onCanceled(Exception mLastError) {
        if (searchView != null) {
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    searchView.showGooglePlayServiceErrorDialog(mLastError);
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    searchView.startActivity(mLastError);
                } else {
                    searchView.updateText("The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                searchView.updateText("Request cancelled.");
            }
        }
    }
}


