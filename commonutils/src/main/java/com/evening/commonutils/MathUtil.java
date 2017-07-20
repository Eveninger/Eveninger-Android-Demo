package com.evening.commonutils;

import android.graphics.PointF;

/**
 * Created by Nighter on 17/7/20.
 */

public class MathUtil {
    /**
     * the distance of two points
     *
     * @param p1
     * @param p2
     * @return
     */
    public static double hypot(PointF p1, PointF p2) {
        return Math.hypot(p1.x - p2.x, p1.y - p2.y);
    }
}
