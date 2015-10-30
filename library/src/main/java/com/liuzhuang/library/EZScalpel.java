package com.liuzhuang.library;

import android.app.Application;

import com.liuzhuang.library.callback.EZActivityLifeCycleCallBack;
import com.liuzhuang.library.log.EZLog;

/**
 * 对Scalpel的封装，自动注册activity
 * Created by liuzhuang on 10/29/15.
 */
public class EZScalpel {
    private static EZScalpel instance = new EZScalpel();

    public static EZScalpel getInstance() {
        return instance;
    }

    private EZScalpel() {
    }

    public void setUp(Application application) {
        if (application == null) {
            EZLog.logi("EZScalpel", "context can not be null");
            return;
        }

        try {
            Application.ActivityLifecycleCallbacks mCallbacks = new EZActivityLifeCycleCallBack();
            application.registerActivityLifecycleCallbacks(mCallbacks);
        } catch (Throwable t) {
            EZLog.loge("EZScalpel", t.getMessage());
        }

    }
}
