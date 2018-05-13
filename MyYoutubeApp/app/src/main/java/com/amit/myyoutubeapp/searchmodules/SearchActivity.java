package com.amit.myyoutubeapp.searchmodules;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amit.myyoutubeapp.BaseActivity;
import com.amit.myyoutubeapp.R;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.youtube.model.SearchListResponse;

public class SearchActivity extends BaseActivity implements SearchContract.ISearchView {

    private SearchContract.ISearchPresenter searchPresenter;
    private SearchListResponse mSearchListResponse;
    private ProgressBar progress;
    private TextView messageText;
    private LinearLayoutManager mLayoutManager;
    private VideoListAdapter videoListAdapter;
    private boolean mLoading = true;
    private RecyclerView mRecyclerView;
    private String searchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchText = getIntent().getStringExtra(SEARCH_KEY);
        getSupportActionBar().setTitle("Search Video - " + searchText);
        progress = findViewById(R.id.progress);
        messageText = findViewById(R.id.messageText);
        searchPresenter = new SearchPresenter();
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.addOnScrollListener(onScrollListener);
        mSearchListResponse = new SearchListResponse();
        videoListAdapter = new VideoListAdapter(this, true);
        videoListAdapter.setVideoVmList(mSearchListResponse);
        mRecyclerView.setAdapter(videoListAdapter);
    }

    /**
     * Recycle-view Scroll Listener.
     */
    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            //check for scroll down
            if (dy > 0) {
                int visibleItemCount = mLayoutManager.getChildCount();
                int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                if (mLoading) {
                    int lastItem = (visibleItemCount + pastVisibleItems);
                    if (lastItem >= mSearchListResponse.getItems().size()) {
                        mLoading = false;
                        //Do pagination.. i.e. fetch new data
                        getBaseAuthPermission();
                    }
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        searchPresenter.addView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBaseAuthPermission();
    }


    @Override
    protected void onStop() {
        super.onStop();
        searchPresenter.removeView();
    }


    @Override
    public void performApiCall() {
        if (searchText != null) {
            searchPresenter.performSearch(mCredential, searchText, mSearchListResponse.getNextPageToken());
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getBaseAuthPermission();
                }
                break;
        }
    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showGooglePlayServiceErrorDialog(Exception mLastError) {
        showGooglePlayServicesAvailabilityErrorDialog(
                ((GooglePlayServicesAvailabilityIOException) mLastError)
                        .getConnectionStatusCode());
    }

    @Override
    public void startActivity(Exception mLastError) {
        startActivityForResult(((UserRecoverableAuthIOException) mLastError).getIntent(), SearchActivity.REQUEST_AUTHORIZATION);
    }

    @Override
    public void updateText(String str) {
        messageText.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        messageText.setText(str);
    }

    @Override
    public void loadData(SearchListResponse searchListResponse) {
        if (searchListResponse != null) {
            mSearchListResponse.setNextPageToken(searchListResponse.getNextPageToken());
            if (mSearchListResponse.getItems() == null) {
                mSearchListResponse.setItems(searchListResponse.getItems());
            } else {
                mSearchListResponse.getItems().addAll(searchListResponse.getItems());
            }
            messageText.setVisibility(View.GONE);
            videoListAdapter.setVideoVmList(mSearchListResponse);
            videoListAdapter.notifyDataSetChanged();
        }
        //enabling pagination
        mLoading = true;
    }
}
