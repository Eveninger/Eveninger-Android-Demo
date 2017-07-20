package com.evening.bezier.controlbezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.evening.commonutils.DisplayMetricsUtil;

/**
 * Created by Nighter on 17/7/20.
 */

public class CubicBezierView extends View {
    private Paint mPaint;
    private Path mBezierPath;
    private PointF mStartPos;
    private PointF mControlPos1;
    private PointF mControlPos2;
    private boolean mIsSecondPointer = false;

    private PointF mEndPos;

    public CubicBezierView(Context context) {
        this(context, null);
    }

    public CubicBezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CubicBezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBezierPath = new Path();

        mStartPos = new PointF(0, 0);
        mControlPos1 = new PointF(0, 0);
        mControlPos2 = new PointF(0, 0);
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
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                mIsSecondPointer = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mIsSecondPointer = false;
                break;
            case MotionEvent.ACTION_MOVE:
                mControlPos1.set(event.getX(0), event.getY(0));
                if (mIsSecondPointer) {
                    mControlPos2.set(event.getX(1), event.getY(1));
                }
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCubicPoints(canvas);
        drawSublines(canvas);
        drawCubicBezier(canvas);
    }

    private void drawCubicPoints(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DisplayMetricsUtil.dp2px(getContext(), 5));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.BLACK);

        canvas.drawPoint(mStartPos.x, mStartPos.y, mPaint);
        canvas.drawPoint(mEndPos.x, mEndPos.y, mPaint);
        canvas.drawPoint(mControlPos1.x, mControlPos1.y, mPaint);
        canvas.drawPoint(mControlPos2.x, mControlPos2.y, mPaint);
    }

    private void drawSublines(Canvas canvas) {
        mPaint.setStrokeWidth(0);
        mPaint.setColor(Color.BLACK);

        canvas.drawLine(mStartPos.x, mStartPos.y, mControlPos1.x, mControlPos1.y, mPaint);
        canvas.drawLine(mControlPos1.x, mControlPos1.y, mControlPos2.x, mControlPos2.y, mPaint);
        canvas.drawLine(mControlPos2.x, mControlPos2.y, mEndPos.x, mEndPos.y, mPaint);
    }

    private void drawCubicBezier(Canvas canvas) {
        mPaint.setStrokeWidth(DisplayMetricsUtil.dp2px(getContext(), 3));
        mPaint.setColor(Color.RED);

        mBezierPath.reset();
        mBezierPath.moveTo(mStartPos.x, mStartPos.y);
        mBezierPath.cubicTo(mControlPos1.x, mControlPos1.y, mControlPos2.x, mControlPos2.y, mEndPos.x, mEndPos.y);
        canvas.drawPath(mBezierPath, mPaint);
    }
}
