package com.evening.bezier.qqbubble;

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
import com.evening.commonutils.MathUtil;

/**
 * Created by Nighter on 17/7/20.
 */

public class QQBubbleView extends View {
    private Paint mPaint;
    private Paint mTextPaint;
    private Path mPath;

    private PointF mCircleCenter;
    private PointF mBubbleCenter;
    private PointF mControlPoint;

    private PointF mCircleStart;
    private PointF mBubbleEnd;
    private PointF mBubbleStart;
    private PointF mCircleEnd;

    private float mCircleRadius;
    private float mBubbleRadius;


    private float mDistance;
    private float mSin, mCos;

    public QQBubbleView(Context context) {
        this(context, null, 0);
    }

    public QQBubbleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQBubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(DisplayMetricsUtil.dp2px(context, 12));

        mPath = new Path();

        mCircleCenter = new PointF();
        mBubbleCenter = new PointF();
        mControlPoint = new PointF();

        mCircleStart = new PointF();
        mBubbleEnd = new PointF();
        mBubbleStart = new PointF();
        mCircleEnd = new PointF();

        mCircleRadius = DisplayMetricsUtil.dp2px(context, 12);
        mBubbleRadius = mCircleRadius;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCircleCenter.set(w / 2, h / 2);
        mBubbleCenter.set(w / 2, h / 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mBubbleCenter.set(event.getX(), event.getY());
                calculateBezierCoordinates();
                invalidate();
                break;
        }
        return true;
    }

    private void calculateBezierCoordinates() {
        //Bezier控制点坐标
        mControlPoint.set((mBubbleCenter.x + mCircleCenter.x) / 2,
                (mBubbleCenter.y + mCircleCenter.y) / 2);
        mDistance = (float) MathUtil.hypot(mBubbleCenter, mCircleCenter);
        mCircleRadius = mBubbleRadius - mDistance / 8;

                //Bezier起点坐标
        mSin = (mBubbleCenter.y - mCircleCenter.y) / mDistance;
        mCos = (mBubbleCenter.x - mCircleCenter.x) / mDistance;

        mCircleStart.set(
                mCircleCenter.x + mCircleRadius * mSin,
                mCircleCenter.y - mCircleRadius * mCos
        );
        mBubbleEnd.set(
                mBubbleCenter.x + mBubbleRadius * mSin,
                mBubbleCenter.y - mBubbleRadius * mCos
        );
        mBubbleStart.set(
                mBubbleCenter.x - mBubbleRadius * mSin,
                mBubbleCenter.y + mBubbleRadius * mCos
        );
        mCircleEnd.set(
                mCircleCenter.x - mCircleRadius * mSin,
                mCircleCenter.y + mCircleRadius * mCos
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDebugInfo(canvas);
        drawPoints(canvas);
        drawPath(canvas);
    }

    private void drawPoints(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DisplayMetricsUtil.dp2px(getContext(), 3));
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaint.setColor(Color.BLACK);
        canvas.drawPoint(mCircleCenter.x, mCircleCenter.y, mPaint);
        canvas.drawPoint(mBubbleCenter.x, mBubbleCenter.y, mPaint);

        mPaint.setColor(Color.RED);
        canvas.drawPoint(mControlPoint.x, mControlPoint.y, mPaint);

        mPaint.setColor(Color.YELLOW);
        canvas.drawPoint(mCircleStart.x, mCircleStart.y, mPaint);

        mPaint.setColor(Color.BLUE);
        canvas.drawPoint(mBubbleEnd.x, mBubbleEnd.y, mPaint);

        mPaint.setColor(Color.GREEN);
        canvas.drawPoint(mBubbleStart.x, mBubbleStart.y, mPaint);

        mPaint.setColor(Color.CYAN);
        canvas.drawPoint(mCircleEnd.x, mCircleEnd.y, mPaint);
    }

    private void drawPath(Canvas canvas) {
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);

        canvas.drawCircle(mCircleCenter.x, mCircleCenter.y, mCircleRadius, mPaint);
        canvas.drawCircle(mBubbleCenter.x, mBubbleCenter.y, mBubbleRadius, mPaint);

        mPath.reset();
        mPath.moveTo(mCircleStart.x, mCircleStart.y);
        mPath.quadTo(mControlPoint.x, mControlPoint.y, mBubbleEnd.x, mBubbleEnd.y);
        mPath.lineTo(mBubbleStart.x, mBubbleStart.y);
        mPath.quadTo(mControlPoint.x, mControlPoint.y, mCircleEnd.x, mCircleEnd.y);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    private void drawDebugInfo(Canvas canvas) {
        canvas.drawText("Distance: " + mDistance + " Sin: " + mSin + " Cos: " + mCos, 10, 50, mTextPaint);
    }
}
