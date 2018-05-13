package com.amit.myyoutubeapp.searchmodules;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amit.myyoutubeapp.AppApplication;
import com.amit.myyoutubeapp.R;
import com.bumptech.glide.Glide;
import com.google.api.services.youtube.model.SearchListResponse;

/**
 * VideoListAdapter.
 */

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private static final long DOUBLE_CLICK_TIME_DELTA = 300;
    private long lastClickTime = 0;

    private boolean tappingAllow = false;


    private final String TAG = VideoListAdapter.class.getSimpleName();
    private SearchListResponse searchListResponse;
    private Context context;
    //private OnListItemListener onListItemListener;

    public VideoListAdapter(Context context, boolean tappingAllow) {
        this.context = context;
        this.tappingAllow = tappingAllow;
    }

    public void setVideoVmList(SearchListResponse searchListResponse) {
        this.searchListResponse = searchListResponse;
    }

   /* public OnListItemListener getOnListItemListener() {
        return onListItemListener;
    }

    public void setOnListItemListener(OnListItemListener onListItemListener) {
        this.onListItemListener = onListItemListener;
    }

    public void removeListener() {
        this.onListItemListener = null;
    }*/

    @NonNull
    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.watch_listing, parent, false);
        return new VideoListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoListAdapter.ViewHolder holder, final int position) {
        Glide.with(holder.networkImageView.getContext()).load(searchListResponse.getItems().get(position).getSnippet().getThumbnails().getDefault().getUrl()).into(holder.networkImageView);
        holder.title.setText(searchListResponse.getItems().get(position).getSnippet().getTitle());
        holder.watch_listing_main.setOnClickListener((View view) -> {
            if (tappingAllow) {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    lastClickTime = 0;
                    AppApplication.getInstance().getSearchResults().add(searchListResponse.getItems().get(position));
                } else {
                    AppApplication.getInstance().getSearchResults().remove(searchListResponse.getItems().get(position));
                }
                lastClickTime = clickTime;
            } else {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + searchListResponse.getItems().get(position).getId().getVideoId()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + searchListResponse.getItems().get(position).getId().getVideoId()));
                try {
                    context.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(webIntent);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "Total Item = " + searchListResponse.size());
        if (searchListResponse.getItems() != null) {
            return searchListResponse.getItems().size();
        }
        return 0;
    }

    /**
     * Recycleview View-Holder pattern.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView networkImageView;
        TextView title;
        LinearLayout watch_listing_main;

        ViewHolder(View itemView) {
            super(itemView);
            networkImageView = itemView.findViewById(R.id.watch_listing_thumbnail);
            title = itemView.findViewById(R.id.watch_listing_title);
            watch_listing_main = itemView.findViewById(R.id.watch_listing_main);
        }
    }
}
