package com.evening.bezier.controlbezier;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.evening.commonutils.DisplayMetricsUtil;

/**
 * Created by Nighter on 17/7/20.
 */

public class QuadBezierView extends View {
    private Paint mPaint;
    private Path mBezierPath;
    private PointF mStartPos;
    private PointF mControlPos;
    private PointF mEndPos;

    public QuadBezierView(Context context) {
        this(context, null);
    }

    public QuadBezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuadBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBezierPath = new Path();

        mStartPos = new PointF(0, 0);
        mControlPos = new PointF(0, 0);
        mEndPos = new PointF(0, 0);
        post(new Runnable() {
            @Override
            public void run() {
                mStartPos.set(DisplayMetricsUtil.dp2px(context, 20), getHeight() / 2);
                mEndPos.set(getWidth() - DisplayMetricsUtil.dp2px(context, 20), getHeight() / 2);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mControlPos.set(event.getX(), event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                ValueAnimator animX = ValueAnimator.ofFloat(mControlPos.x, getWidth() / 2);
                animX.setDuration(500);
                animX.setInterpolator(new OvershootInterpolator());
                animX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mControlPos.set((Float) animation.getAnimatedValue(), mControlPos.y);
                        invalidate();
                    }
                });
                animX.start();

                ValueAnimator animY = ValueAnimator.ofFloat(mControlPos.y, getHeight() / 2);
                animY.setDuration(500);
                animY.setInterpolator(new OvershootInterpolator());
                animY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mControlPos.set(mControlPos.x, (Float) animation.getAnimatedValue());
                        invalidate();
                    }
                });
                animY.start();
                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawQuadPoints(canvas);
        drawSublines(canvas);
        drawQuadBezier(canvas);
    }

    private void drawQuadPoints(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DisplayMetricsUtil.dp2px(getContext(), 5));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.BLACK);

        canvas.drawPoint(mStartPos.x, mStartPos.y, mPaint);
        canvas.drawPoint(mEndPos.x, mEndPos.y, mPaint);
        canvas.drawPoint(mControlPos.x, mControlPos.y, mPaint);
    }

    private void drawSublines(Canvas canvas) {
        mPaint.setStrokeWidth(0);
        mPaint.setColor(Color.BLACK);

        canvas.drawLine(mStartPos.x, mStartPos.y, mControlPos.x, mControlPos.y, mPaint);
        canvas.drawLine(mControlPos.x, mControlPos.y, mEndPos.x, mEndPos.y, mPaint);
    }

    private void drawQuadBezier(Canvas canvas) {
        mPaint.setStrokeWidth(DisplayMetricsUtil.dp2px(getContext(), 3));
        mPaint.setColor(Color.RED);

        mBezierPath.reset();
        mBezierPath.moveTo(mStartPos.x, mStartPos.y);
        mBezierPath.quadTo(mControlPos.x, mControlPos.y, mEndPos.x, mEndPos.y);
        canvas.drawPath(mBezierPath, mPaint);
    }

}

























