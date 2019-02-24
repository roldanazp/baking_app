package com.roldannanodegre.bakingapp.util;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;

public class DeviceUtils {

    public static boolean isTablet(@Nullable Activity activity){
        if (activity == null) {
            return false;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float scaleFactor = metrics.density;

        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);

        return smallestWidth > 600;
    }
}
