package com.digitalvm.vmnews.controller;

import com.digitalvm.vmnews.model.NewsObjectResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by TL on 5/16/17.
 */

public interface ApiInterface {

    @GET("articles?")
    Call<NewsObjectResponse> getNews(@Query("source") String source, @Query("apiKey") String apiKey);

}
