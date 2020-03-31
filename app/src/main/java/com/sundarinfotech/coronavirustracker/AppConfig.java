package com.sundarinfotech.coronavirustracker;

import android.app.Application;

import com.sundarinfotech.coronavirustracker.util.NetworkHelper;

public class AppConfig extends Application {

    private void initialize() {
        // network helper
        NetworkHelper.setContext(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initialize();
    }
}