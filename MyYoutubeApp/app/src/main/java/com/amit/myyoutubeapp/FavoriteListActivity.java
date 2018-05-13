package com.amit.myyoutubeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.amit.myyoutubeapp.searchmodules.SearchActivity;
import com.amit.myyoutubeapp.searchmodules.VideoListAdapter;
import com.google.api.services.youtube.model.SearchListResponse;

public class FavoriteListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        getSupportActionBar().setTitle("Favorite Video List");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

    }

    @Override
    protected void onResume() {
        super.onResume();
        SearchListResponse mSearchListResponse = new SearchListResponse();
        mSearchListResponse.setItems(AppApplication.getInstance().getSearchResults());
        if (AppApplication.getInstance().getSearchResults().size() > 0) {
            VideoListAdapter videoListAdapter = new VideoListAdapter(this, false);
            videoListAdapter.setVideoVmList(mSearchListResponse);
            mRecyclerView.setAdapter(videoListAdapter);
            findViewById(R.id.messageText).setVisibility(View.GONE);
        } else {
            findViewById(R.id.messageText).setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void performApiCall() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        // Retrieve the SearchView and plug it into SearchManager
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(getResources().getString(R.string.str_search_msg));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    callSearch(query);
                } else {
                    Toast.makeText(FavoriteListActivity.this, "Hello A", Toast.LENGTH_LONG).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    public void callSearch(String query) {
        //Do searching
        Intent intent = new Intent(FavoriteListActivity.this, SearchActivity.class);
        intent.putExtra(SEARCH_KEY, query);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
