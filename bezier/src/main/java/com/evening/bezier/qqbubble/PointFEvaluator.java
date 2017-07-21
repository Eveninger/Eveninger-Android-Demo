package com.evening.bezier.qqbubble;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by Nighter on 17/7/20.
 */

public class PointFEvaluator implements TypeEvaluator<PointF> {
    private PointF mResult = new PointF();

    @Override
    public PointF evaluate(float fraction, PointF startPointF, PointF endPointF) {
        float x = startPointF.x + fraction * (endPointF.x - startPointF.x);
        float y = startPointF.y + fraction * (endPointF.y - startPointF.y);
        mResult.set(x, y);
        return mResult;
    }
}
