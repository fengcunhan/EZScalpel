package com.liuzhuang.library.callback;

import android.content.Context;
import android.text.TextUtils;

import com.liuzhuang.library.utils.EZSharedPreferenceUtil;

import java.util.Set;

/**
 * Created by liuzhuang on 11/4/15.
 */
public class EZScalpelFilter {
    public static boolean filter(Context context, String name) {
        if (context == null) {
            return false;
        }
        if (EZSharedPreferenceUtil.getSwitcherState(context)) {
            return true;
        }
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        Set<String> data = EZSharedPreferenceUtil.getActivityNameSet(context);
        if (data != null && data.contains(name)) {
            return true;
        }
        return false;
    }
}
