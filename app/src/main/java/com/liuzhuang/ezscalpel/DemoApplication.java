package com.liuzhuang.ezscalpel;

import android.app.Application;

import com.liuzhuang.library.EZScalpel;

/**
 * Created by liuzhuang on 10/29/15.
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EZScalpel.getInstance().setUp(this);
    }
}
