package com.easyguide;

import android.app.Application;

import com.estimote.sdk.EstimoteSDK;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        EstimoteSDK.initialize(getApplicationContext(), BuildConfig.ESTIMOTE_APP_ID, BuildConfig.ESTIMOTE_APP_TOKEN);
    }
}
