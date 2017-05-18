package com.digitalvm.vmnews;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.digitalvm.vmnews.adapter.NewsAdapterRecycle;
import com.digitalvm.vmnews.controller.ApiClient;
import com.digitalvm.vmnews.controller.ApiInterface;
import com.digitalvm.vmnews.model.NewsObject;
import com.digitalvm.vmnews.model.NewsObjectResponse;
import com.digitalvm.vmnews.util.VMUtils;
import com.digitalvm.vmnews.views.EndlessRecyclerOnScrollListener;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    public RecyclerView lv_news;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<NewsObject> listNews, listNewsCache;
    private NewsAdapterRecycle newsAdapter;
    private LinearLayoutManager linearLayoutManager;

    private static final String CURRENT_POSITION = "current_position";
    private int mPosition;

    public ImageLoader imgLoader;
    public DisplayImageOptions options;

    private int currentIndex = 0;
    private final int PAGE_LIMIT = 4;
    private boolean isEndOfRecord = false;
    private boolean isListRefreshed = false;
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_news = (RecyclerView) findViewById(R.id.lv_news);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        setupImageLoader();

        // use a linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        lv_news.setLayoutManager(linearLayoutManager);
        lv_news.setItemAnimator(new DefaultItemAnimator());
        // If possible, make all elements of the RecyclerView with the same height
        lv_news.setHasFixedSize(true);

        lv_news.setItemViewCacheSize(20);
        lv_news.setDrawingCacheEnabled(true);
        lv_news.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isListRefreshed = true;
                swipeRefreshLayout.setRefreshing(true);
                bindNewsToList(true, isListRefreshed);
            }
        });

        boolean pauseOnScroll = false;
        boolean pauseOnFling = true;
        lv_news.addOnScrollListener(new EndlessRecyclerOnScrollListener(
                linearLayoutManager, imgLoader, pauseOnScroll, pauseOnFling) {
            @Override
            public void onLoadMore(int current_page) {
                if (!isEndOfRecord)
                    bindNewsToList(false, false);
            }

        });

        newsAdapter = new NewsAdapterRecycle(MainActivity.this);
        lv_news.setAdapter(newsAdapter);
        loadNews();

    }

    private void setupImageLoader() {

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .delayBeforeLoading(1000)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(MainActivity.this);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.diskCacheExtraOptions(480, 320, null);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        imgLoader = ImageLoader.getInstance();
        imgLoader.init(config.build());

    }


    private void loadNews() {
        if (VMUtils.isConnectingToInternet(MainActivity.this)) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            String source = "techcrunch" ;

            apiService.getNews(source, VMNewsApp.WEBSERVICE_KEY).enqueue(new Callback<NewsObjectResponse>() {
                @Override
                public void onResponse(Call<NewsObjectResponse> call, Response<NewsObjectResponse> response) {

                    if (response.isSuccessful()) {
                        listNewsCache = response.body().getArticles();
                        bindNewsToList(true, false);

                    } else {
                        int statusCode = response.code();
                    }
                }

                @Override
                public void onFailure(Call<NewsObjectResponse> call, Throwable t) {
                    call.cancel();
                    Log.e(TAG, t.toString());
                }
            });

        } else {
            VMUtils.showAlertDialogNoInternet(MainActivity.this);

        }

    }

    private void bindNewsToList(final boolean isReload, final boolean isRefreshed) {

        int tempCurrentIndex;
        if (isReload && !isRefreshed) {
            // Table Reload
            tempCurrentIndex = currentIndex = 0;
            isEndOfRecord = false;
            listNews = new ArrayList<>();
            newsAdapter.setList(listNews);

        } else if (isReload & isRefreshed) {
            // Table Refresh
            tempCurrentIndex = 0;
        } else {
            // Table Load More
            tempCurrentIndex = currentIndex;
        }

        int size = listNewsCache.size();
        int incremental = 0;

        if (tempCurrentIndex + PAGE_LIMIT <= size) {
            incremental = tempCurrentIndex + PAGE_LIMIT;

        } else {
            incremental = size - 1;

        }

        List<NewsObject> listTemp = listNewsCache.subList(tempCurrentIndex, incremental);

        if (listTemp != null) {
            if (isListRefreshed) {
                isListRefreshed = false;
                swipeRefreshLayout.setRefreshing(false);

                int result = compareLatestNews();

                if (result > 0) {
                    isEndOfRecord = false;
                    loadNews();

                }

            } else {
                listNews.addAll(listTemp);

                if (listTemp.size() == PAGE_LIMIT && listTemp.size() < listNews.size()) {
                    isEndOfRecord = false;
                    currentIndex = currentIndex + PAGE_LIMIT;

                } else {
                    isEndOfRecord = true;

                }

                mPosition = linearLayoutManager.findLastVisibleItemPosition();
                newsAdapter.setList(listNews);
                linearLayoutManager.scrollToPosition(mPosition);

            }

        } else {
            isEndOfRecord = true;

            if (isListRefreshed) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }

    }

    private int compareLatestNews() {
        int result = 0;
        if (listNews != null && !listNewsCache.isEmpty()) {
            for (int i = 0; i < listNewsCache.size(); i++) {
                if (listNewsCache.get(i).getPublishAtDate() != null && listNews.get(0).getPublishAtDate() != null)
                    result = VMUtils.compareDatetime(listNewsCache.get(i).getPublishAtDate(), listNews.get(0).getPublishAtDate());
                return result;
            }
        }

        return result;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mPosition = linearLayoutManager.findLastVisibleItemPosition();
        newsAdapter = new NewsAdapterRecycle(MainActivity.this);
        lv_news.setAdapter(newsAdapter);
        newsAdapter.setList(listNews);
        linearLayoutManager.scrollToPosition(mPosition);
    }

}


