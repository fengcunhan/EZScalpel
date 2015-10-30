package com.liuzhuang.library.utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by liuzhuang on 10/30/15.
 */
public class EZDeviceUtil {
    /**
     * Get screen width.
     * @param context Context to get {@link WindowManager} instance.
     * @return Screen width.
     */
    public static int getScreenWidth(Context context) {
        Display display = getDisplay(context);

        if (display != null) {
            return display.getWidth();
        }

        return 0;
    }

    /**
     * Get screen height.
     * @param context Used to get {@link WindowManager} instance.
     * @return Screen height.
     */
    public static int getScreenHeight(Context context) {
        Display display = getDisplay(context);

        if (display != null) {
            return display.getHeight();
        }

        return 0;
    }


    /**
     * Get the {@link Display} instance, witch contains information about size and density.
     * @param context Context to get {@code WindowManager} instance.
     * @return  The {@link Display} instance.
     */
    public static Display getDisplay(Context context) {
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);

        if (windowManager == null) {
            return null;
        }

        return windowManager.getDefaultDisplay();
    }
}
