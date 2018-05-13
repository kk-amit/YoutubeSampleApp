package com.amit.myyoutubeapp.searchmodules;

import com.google.api.services.youtube.model.SearchListResponse;

/**
 * SearchResponseData.
 */

public class SearchResponseData {
    private SearchListResponse searchListResponse;
    private Exception exception;

    public SearchListResponse getSearchListResponse() {
        return searchListResponse;
    }

    public void setSearchListResponse(SearchListResponse searchListResponse) {
        this.searchListResponse = searchListResponse;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
