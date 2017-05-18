package com.digitalvm.vmnews.views;

/**
 * Created by TL on 7/31/16.
 */

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;

import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class EndlessRecyclerOnScrollListener extends
        RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class
            .getSimpleName();

    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private ImageLoader imageLoader;
    private final boolean pauseOnScroll;
    private final boolean pauseOnFling;

    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(
            LinearLayoutManager mLinearLayoutManager, ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
        this.mLinearLayoutManager = mLinearLayoutManager;
        this.imageLoader = imageLoader;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (mLinearLayoutManager != null) {
            visibleItemCount = recyclerView.getChildCount();
            totalItemCount = mLinearLayoutManager.getItemCount();
            firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading
                    && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached

                // Do something
                current_page++;

                onLoadMore(current_page);

                loading = true;
            }
        }
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        switch (newState) {
            case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                imageLoader.resume();
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                if (pauseOnScroll) {
                    imageLoader.pause();
                }
                break;
            case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                if (pauseOnFling) {
                    imageLoader.pause();
                }
                break;
        }

    }

    public abstract void onLoadMore(int current_page);
}