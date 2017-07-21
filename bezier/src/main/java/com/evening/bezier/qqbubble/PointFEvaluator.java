package com.evening.bezier.qqbubble;

import android.animation.TypeEvaluator;
import android.graphics.PointF;
import android.util.Log;

/**
 * Created by Nighter on 17/7/20.
 */

public class PointFEvaluator implements TypeEvaluator<PointF> {
    private static final String TAG = "PointFEvaluator";

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        float x = startValue.x + fraction * (endValue.x - startValue.x);
        float y = startValue.y + fraction * (endValue.y - startValue.y);
        PointF pointF = new PointF(x, y);
        Log.d(TAG, "start = " + startValue + " end = " + endValue);
        return pointF;
    }
}
