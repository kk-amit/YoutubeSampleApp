package com.amit.myyoutubeapp.searchmodules;

import com.google.api.services.youtube.model.SearchListResponse;

/**
 * IAPIListener.
 */

public interface IAPIListener {

    void showProgress();

    void hideProgress();

    void onCompleted(SearchResponseData output);

    void onCanceled(Exception ex);
}
