package com.digitalvm.vmnews.MockRetrofit;

import com.digitalvm.vmnews.VMNewsApp;
import com.digitalvm.vmnews.controller.ApiInterface;
import com.digitalvm.vmnews.model.NewsObject;
import com.digitalvm.vmnews.model.NewsObjectResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Query;
import retrofit2.mock.BehaviorDelegate ;


/**
 * Created by TL on 5/18/17.
 */

public class MockNewsService implements ApiInterface {
    private final BehaviorDelegate<ApiInterface> delegate;

    public MockNewsService(BehaviorDelegate<ApiInterface> service) {
        this.delegate = service;
    }

    @Override
    public Call<NewsObjectResponse> getNews(@Query("source") String source, @Query("apiKey") String apiKey) {
        NewsObjectResponse newsObjectResponse = new NewsObjectResponse();

        ArrayList<NewsObject> listNews = new ArrayList<>();
        NewsObject newsObject = new NewsObject();
        newsObject.setTitle("Everything Google announced at its Google I/O keynote");
        newsObject.setAuthor("Romain Dillet");
        listNews.add(newsObject);
        newsObjectResponse.setArticles(listNews);

        return delegate.returningResponse(newsObjectResponse).getNews("cnn", VMNewsApp.WEBSERVICE_KEY);

    }
}
