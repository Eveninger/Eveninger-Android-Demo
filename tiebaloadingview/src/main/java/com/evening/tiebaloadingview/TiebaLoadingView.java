package com.evening.tiebaloadingview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Nighter on 17/7/18.
 * Source: http://www.jianshu.com/p/c8e70e045133
 */

public class TiebaLoadingView extends View {
    private static final String TAG = "Practice12TiebaLoadingV";
    private Paint mTextPaint;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private Path mCirclePath;
    private Path mWavePath;
    private int mTextColor;
    private int mWaveColor;

    private float mPercent;

    public TiebaLoadingView(Context context) {
        this(context, null, 0);
    }

    public TiebaLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TiebaLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TiebaLoadingView);
        mTextColor = array.getColor(R.styleable.TiebaLoadingView_text_color, Color.GREEN);
        mWaveColor = array.getColor(R.styleable.TiebaLoadingView_wave_color, Color.YELLOW);
        array.recycle();

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setColor(mTextColor);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mWaveColor);
        mPaint.setDither(true);

        mCirclePath = new Path();
        mWavePath = new Path();

        post(new Runnable() {
            @Override
            public void run() {
                mWidth = getWidth();
                mHeight = getHeight();
                mCirclePath.addCircle(mWidth / 2, mHeight / 2, mWidth < mHeight ? mWidth / 2 : mHeight / 2, Path.Direction.CCW);
                mTextPaint.setTextSize(mWidth / 2);
            }
        });

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPercent = animation.getAnimatedFraction();
                invalidate();
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mTextPaint.setColor(mTextColor);
        drawCenterText(canvas, mTextPaint, "夜");

        calculateWavePath(mPercent);
        canvas.clipPath(mWavePath);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth < mHeight ? mWidth / 2 : mHeight / 2, mPaint);
        mTextPaint.setColor(Color.WHITE);
        drawCenterText(canvas, mTextPaint, "夜");
    }

    private void calculateWavePath(float percent) {
        mWavePath.reset();
        Log.d(TAG, "percent = " + percent);
        int start = (int) ((percent - 1) * mWidth);

        int quadWidth = (int) (mWidth / 4.0f);
        int quadHeight = (int) (mHeight / 20.0f * 3);

        mWavePath.moveTo(start, mHeight / 2);
        //第一个波形
        mWavePath.rQuadTo(quadWidth, quadHeight, quadWidth * 2, 0);
        mWavePath.rQuadTo(quadWidth, -quadHeight, quadWidth * 2, 0);
        //第二个波形
        mWavePath.rQuadTo(quadWidth, quadHeight, quadWidth * 2, 0);
        mWavePath.rQuadTo(quadWidth, -quadHeight, quadWidth * 2, 0);

        mWavePath.lineTo(mWidth, mHeight);
        mWavePath.lineTo(start, mHeight);
        mWavePath.close();
    }

    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;

        int centerY = (int) ((getHeight() - top - bottom) / 2);

        canvas.drawText(text, getWidth() / 2, centerY, paint);
    }
}
