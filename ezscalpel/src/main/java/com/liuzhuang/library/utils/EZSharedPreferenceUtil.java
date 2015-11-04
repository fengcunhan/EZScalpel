package com.liuzhuang.library.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.liuzhuang.library.log.EZLog;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by liuzhuang on 11/4/15.
 */
public class EZSharedPreferenceUtil {
    private static final String SP_NAME = "ezscalpel_sp";
    private static final String SP_ACTIVITY_NAME_KEY = "ezscalpel_activity_name_key";
    private static final String SP_SWITCH_STATE_KEY = "ezscalpel_switch_state_key";

    public static Set<String> getActivityNameSet(Context context) {
        try {
            Context mContext = context.getApplicationContext();
            Set<String> dataSet = new LinkedHashSet<String>();
            SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            if (sp != null) {
                Set<String> temp = sp.getStringSet(SP_ACTIVITY_NAME_KEY, null);
                if (temp != null) {
                    dataSet.addAll(temp);
                }
                return dataSet;
            }
        } catch (Throwable e) {
            EZLog.loge("getActivityNameSet", e.getMessage());
        }
        return null;
    }

    public static void addActivityName(Context context, String name) {
        try {
            Context mContext = context.getApplicationContext();
            SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
            Set<String> nameSet = getActivityNameSet(mContext);
            if (nameSet == null) {
                nameSet = new LinkedHashSet<String>();
            }
            nameSet.add(name);
            sp.edit().putStringSet(SP_ACTIVITY_NAME_KEY, nameSet).commit();
        } catch (Throwable e) {
            EZLog.loge("addActivityName", e.getMessage());
        }
    }

    public static void deleteActivityName(Context context, String name) {
        if (context == null) {
            return;
        }

        try {
            Context mContext = context.getApplicationContext();
            Set<String> dataSet = getActivityNameSet(mContext);
            if (dataSet != null) {
                dataSet.remove(name);
                SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
                sp.edit().putStringSet(SP_ACTIVITY_NAME_KEY, dataSet).commit();
            }
        } catch (Throwable e) {
            EZLog.loge("deleteActivityName", e.getMessage());
        }
    }

    public static void setSwitcherState(Context context, boolean closeFilter) {
        if (context == null) {
            return;
        }
        Context mContext = context.getApplicationContext();
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (sp != null) {
            sp.edit().putBoolean(SP_SWITCH_STATE_KEY, closeFilter).commit();
        }
    }

    public static boolean getSwitcherState(Context context) {
        if (context == null) {
            return false;
        }
        Context mContext = context.getApplicationContext();
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        if (sp != null) {
            return sp.getBoolean(SP_SWITCH_STATE_KEY, false);
        }
        return false;
    }
}
