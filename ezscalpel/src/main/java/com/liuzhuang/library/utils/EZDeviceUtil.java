package com.liuzhuang.library.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by liuzhuang on 10/30/15.
 */
public class EZDeviceUtil {
    private static float sDisplayDensity = -1;

    /**
     * Get screen width.
     *
     * @param context Context to get {@link WindowManager} instance.
     * @return Screen width.
     */
    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        }
        Display display = getDisplay(context);

        if (display != null) {
            return display.getWidth();
        }

        return 0;
    }

    /**
     * Get screen height.
     *
     * @param context Used to get {@link WindowManager} instance.
     * @return Screen height.
     */
    public static int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        }
        Display display = getDisplay(context);

        if (display != null) {
            return display.getHeight();
        }

        return 0;
    }


    /**
     * Get the {@link Display} instance, witch contains information about size and density.
     *
     * @param context Context to get {@code WindowManager} instance.
     * @return The {@link Display} instance.
     */
    private static Display getDisplay(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (windowManager == null) {
            return null;
        }

        return windowManager.getDefaultDisplay();
    }

    /**
     * dp to px
     */
    public static int dp2px(Context context, float dpValue) {
        if (context == null) {
            return 0;
        }
        final float scale = getDisplayDensity(context);
        int result = (int) (dpValue * scale + 0.5f);

        return result;
    }

    /**
     * px to dp
     */
    public static int px2dp(Context context, float pxValue) {
        if (context == null) {
            return 0;
        }
        final float scale = getDisplayDensity(context);
        int result = (int) (pxValue / scale + 0.5f);

        return result;
    }

    /**
     * @return {@link android.util.DisplayMetrics#density}
     */
    private static float getDisplayDensity(Context context) {
        if (sDisplayDensity < 0) {
            sDisplayDensity = context.getResources().getDisplayMetrics().density;
        }

        return sDisplayDensity;

    }

    public static int getNavigationBarHeight(Context context) {
        if (context == null) {
            return 0;
        }
        int orientation = context.getResources().getConfiguration().orientation;
        Resources resources = context.getResources();

        int id = resources.getIdentifier(
                orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape",
                "dimen", "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }
}
