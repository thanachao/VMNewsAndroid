package com.digitalvm.vmnews.MockRetrofit;

import com.digitalvm.vmnews.VMNewsApp;
import com.digitalvm.vmnews.controller.ApiInterface;
import com.digitalvm.vmnews.model.NewsObject;
import com.digitalvm.vmnews.model.NewsObjectResponse;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Query;
import retrofit2.mock.BehaviorDelegate;

/**
 * Created by TL on 5/18/17.
 */

public class MockFailNewsService implements ApiInterface {
    private final BehaviorDelegate<ApiInterface> delegate;

    public MockFailNewsService(BehaviorDelegate<ApiInterface> service) {
        this.delegate = service;
    }

    @Override
    public Call<NewsObjectResponse> getNews(@Query("source") String source, @Query("apiKey") String apiKey) {
        NewsObjectResponse newsObjectResponse = new NewsObjectResponse();
        newsObjectResponse.setStatus("Fail");
        newsObjectResponse.setArticles(null);

        return delegate.returningResponse(newsObjectResponse).getNews("wrongsource", VMNewsApp.WEBSERVICE_KEY);

    }
}
