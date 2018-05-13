package com.amit.myyoutubeapp.searchmodules;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.youtube.model.SearchListResponse;

/**
 * SearchContract.
 */

class SearchContract {
    interface ISearchView {
        void showProgress();

        void hideProgress();

        void showGooglePlayServiceErrorDialog(Exception mLastError);

        void startActivity(Exception mLastError);

        void updateText(String str);

        void loadData(SearchListResponse searchListResponse);
    }

    interface ISearchPresenter {
        void addView(SearchContract.ISearchView searchView);

        void removeView();

        void performSearch(GoogleAccountCredential credential, String... query);


    }

    interface ISearchModel {

    }
}
