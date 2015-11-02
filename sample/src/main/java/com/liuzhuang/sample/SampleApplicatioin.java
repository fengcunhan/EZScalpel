package com.liuzhuang.sample;

import android.app.Application;

import com.liuzhuang.library.EZScalpel;

/**
 * Created by liuzhuang on 10/31/15.
 */
public class SampleApplicatioin extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EZScalpel.getInstance().setUp(this);
    }
}
