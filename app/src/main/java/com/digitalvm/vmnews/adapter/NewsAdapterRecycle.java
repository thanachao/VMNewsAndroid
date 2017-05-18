package com.digitalvm.vmnews.adapter;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.digitalvm.vmnews.MainActivity;
import com.digitalvm.vmnews.R;
import com.digitalvm.vmnews.model.NewsObject;
import com.digitalvm.vmnews.util.VMUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TL on 5/31/2015.
 */
public class NewsAdapterRecycle extends RecyclerView.Adapter<NewsAdapterRecycle.ViewHolder> {
    public MainActivity context;
    private int screenWidth;
    private List<NewsObject> listNews = new ArrayList<>();

    public NewsAdapterRecycle(MainActivity context) {
        this.context = context;
    }

    public void setList(List<NewsObject> listNews) {
        this.listNews = listNews;
        notifyDataSetChanged();
    }

    @Override
    public NewsAdapterRecycle.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new View
        View v = LayoutInflater.from(context).inflate(R.layout.view_news_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final NewsObject newsObject = listNews.get(position);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int orientation = context.getResources().getConfiguration().orientation;
        screenWidth = (displaymetrics.widthPixels);

        if (newsObject.getUrlToImage() != null && !newsObject.getUrlToImage().equals("")) {

            context.imgLoader.displayImage(newsObject.getUrlToImage(), holder.img_news, context.options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.progressbar_loadimage_news.setProgress(0);
                    holder.progressbar_loadimage_news.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    holder.progressbar_loadimage_news.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.progressbar_loadimage_news.setVisibility(View.GONE);

                    if (loadedImage != null) {

                        Bitmap scaledbitmap = VMUtils.scaleBitmapPerWidth(loadedImage, screenWidth);
                        int viewPromoHeight = scaledbitmap.getHeight();
                        int viewPromoWidth = scaledbitmap.getWidth();

                        holder.img_news.getLayoutParams().width = viewPromoWidth;
                        holder.img_news.getLayoutParams().height = viewPromoHeight;
                        holder.img_news.requestLayout();
                        holder.img_news.setImageBitmap(scaledbitmap);

                        if (newsObject.getTitle() != null)
                            holder.txt_news_title.setText(newsObject.getTitle());

                        if (newsObject.getPublishAtDate() != null)
                            holder.txt_posted_time.setText(VMUtils.convertDateToString(newsObject.getPublishAtDate()));
                    }

                }
            }, new ImageLoadingProgressListener() {
                @Override
                public void onProgressUpdate(String imageUri, View view, int current, int total) {
                    holder.progressbar_loadimage_news.setProgress(Math.round(100.0f * current / total));
                }
            });

        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_empty);
            holder.img_news.setImageBitmap(bitmap);

        }


        holder.img_share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                actionShare(newsObject.getTitle(), newsObject.getUrl());
            }

        });

    }

    private void actionShare(String title, String sharedURL) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, title);
        i.putExtra(Intent.EXTRA_TEXT, sharedURL);
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return listNews.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_news, img_share;
        public TextView txt_news_title, txt_posted_time;
        public ProgressBar progressbar_loadimage_news;

        public ViewHolder(View v) {
            super(v);
            img_news = (ImageView) v.findViewById(R.id.img_news);
            img_share = (ImageView) v.findViewById(R.id.img_share);
            txt_news_title = (TextView) v.findViewById(R.id.txt_news_title);
            txt_posted_time = (TextView) v.findViewById(R.id.txt_posted_time);
            progressbar_loadimage_news = (ProgressBar) v.findViewById(R.id.progressbar_loadimage_news);
            itemView.setTag(itemView);

        }

    }

}





