package com.digitalvm.vmnews.controller;

/**
 * Created by TL on 5/16/17.
 */
import com.digitalvm.vmnews.VMNewsApp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = VMNewsApp.WEBSERVICE_ENDPOINT;
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
