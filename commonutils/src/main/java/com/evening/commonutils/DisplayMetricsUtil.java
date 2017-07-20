package com.evening.commonutils;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by evansliu on 2015/3/24.
 */
public class DisplayMetricsUtil {
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获得屏幕宽度，pixels
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获得屏幕高度，pixels
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static float getScaledDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.scaledDensity;
    }


    public static String deleteQuotation(String srcStr) {
        if (srcStr.length() > 0) {
            if (srcStr.substring(0, 1).equals("\"") && srcStr.substring(srcStr.length() - 1, srcStr.length()).equals("\"")) {
                srcStr = srcStr.substring(1, srcStr.length() - 1);
                if (srcStr.substring(0, 1).equals("\'") && srcStr.substring(srcStr.length() - 1, srcStr.length()).equals("\'")) {
                    return srcStr.substring(1, srcStr.length() - 1);
                } else {
                    return srcStr;
                }
            } else {
                return srcStr;
            }
        } else {
            return srcStr;
        }
    }

    public static float getStatusBarHeight(Context context) {
        float statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (Integer) field.get(o);
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    public static int getActionBarHeight(Context context) {
        Rect frame = new Rect();
        //getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }


}
