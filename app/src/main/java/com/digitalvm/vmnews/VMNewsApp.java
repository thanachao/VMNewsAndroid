package com.digitalvm.vmnews;

import android.app.Application;

/**
 * Created by Thanachao on 01/21/2015.
 */

public class VMNewsApp extends Application {

    public static final String WEBSERVICE_ENDPOINT = "https://newsapi.org/v1/" ;
    public static final String WEBSERVICE_KEY = "92dee77d4a1a4046a379b67b59b58eb8" ;

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
