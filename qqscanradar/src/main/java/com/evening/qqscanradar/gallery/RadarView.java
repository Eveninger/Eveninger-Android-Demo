package com.evening.qqscanradar.gallery;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Nighter on 17/7/26.
 */

public class RadarView extends View {
    private Paint mPaint;
    private Shader mScanShader;
    private float mRotateDeg;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        post(new Runnable() {
            @Override
            public void run() {
                mScanShader = new SweepGradient(getWidth() / 2, getHeight() / 2, new int[]{Color.TRANSPARENT, Color.parseColor("#84B5CA")}, null);
            }
        });
        ValueAnimator animator = ValueAnimator.ofFloat(0, 360);
        animator.setDuration(3000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRotateDeg = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setShader(mScanShader);
        canvas.save();
        canvas.rotate(mRotateDeg, getWidth() / 2, getHeight() / 2);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
        canvas.restore();
    }
}
