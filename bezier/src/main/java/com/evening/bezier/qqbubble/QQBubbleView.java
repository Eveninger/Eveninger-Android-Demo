package com.evening.bezier.qqbubble;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.evening.bezier.R;
import com.evening.commonutils.DisplayMetricsUtil;
import com.evening.commonutils.MathUtil;

/**
 * Created by Nighter on 17/7/20.
 */

public class QQBubbleView extends View {
    private static final String TAG = "QQBubbleView";

    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mBitmapPaint;
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
    private float mMaxDistance;

    private int mState;
    private static final int STATE_DEFAULT = 0x00;
    private static final int STATE_DRAG = 0x01;
    private static final int STATE_MOVE = 0x02;
    private static final int STATE_DISMISS = 0x03;

    private static final int[] EXPLOSION_PICS = {
            R.mipmap.explosion_one,
            R.mipmap.explosion_two,
            R.mipmap.explosion_three,
            R.mipmap.explosion_four,
            R.mipmap.explosion_five,
    };
    private Bitmap[] mExplosionBitmaps;
    private Rect mExplosionRect;

    private int mCurExplosionPicIndex = 0;
    private boolean mExplosionAnimEnd = true;

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

        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitmapPaint.setFilterBitmap(true);

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
        mMaxDistance = 8 * mCircleRadius;

        mExplosionBitmaps = new Bitmap[EXPLOSION_PICS.length];
        for (int i = 0; i < mExplosionBitmaps.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), EXPLOSION_PICS[i]);
            mExplosionBitmaps[i] = bitmap;
        }
        mExplosionRect = new Rect();
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
            case MotionEvent.ACTION_DOWN:
                //气泡未消失
                if (mState != STATE_DISMISS) {
                    mDistance = (float) Math.hypot(event.getX() - mBubbleCenter.x, event.getY() - mBubbleCenter.y);
                    if (mDistance < mCircleRadius + mMaxDistance / 4) {
                        mState = STATE_DRAG;
                    } else {
                        mState = STATE_DEFAULT;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mState != STATE_DEFAULT) {
                    mBubbleCenter.set(event.getX(), event.getY());
                    mDistance = (float) MathUtil.hypot(mBubbleCenter, mCircleCenter);
                    //如果是可拖拽的
                    if (mState == STATE_DRAG) {
                        //间距小于可黏连的最大距离
                        if (isConnected()) {
                            calculateBezierCoordinates();
                        }
                        //间距大于可黏连的最大距离
                        else {
                            mState = STATE_MOVE;
                        }
                    }

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mState == STATE_DRAG) {
                    mState = STATE_DEFAULT;
                    setBubbleRestoreAnim();
                } else if (mState == STATE_MOVE) {
                    mState = STATE_DISMISS;
                    setBubbleDismissAnim();
                }
                break;
        }
        return true;
    }

    private void setBubbleRestoreAnim() {
        ValueAnimator animator = ValueAnimator.ofObject(new PointFEvaluator(),
                mBubbleCenter,
                mCircleCenter);
        animator.setDuration(500);
        animator.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                //http://inloop.github.io/interpolator/
                float f = 0.571429f;
                return (float) (Math.pow(2, -4 * input) * Math.sin((input - f / 4) * (2 * Math.PI) / f) + 1);
            }
        });
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF curPoint = (PointF) animation.getAnimatedValue();
                Log.d(TAG, "fraction:" + animation.getAnimatedFraction());
                mBubbleCenter.set(curPoint);
                invalidate();
            }
        });
        animator.start();
    }

    private void setBubbleDismissAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, EXPLOSION_PICS.length);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurExplosionPicIndex = (int) animation.getAnimatedValue() % EXPLOSION_PICS.length;
                Log.d(TAG, "动画索引：" + mCurExplosionPicIndex);
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mExplosionAnimEnd = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mExplosionAnimEnd = true;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mExplosionAnimEnd = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    /**
     * 判断是否黏连
     *
     * @return
     */
    private boolean isConnected() {
        return mDistance < mMaxDistance - mMaxDistance / 4;
    }

    private void calculateBezierCoordinates() {
        //Bezier控制点坐标
        mControlPoint.set((mBubbleCenter.x + mCircleCenter.x) / 2,
                (mBubbleCenter.y + mCircleCenter.y) / 2);
        calculateCircleRadius();

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

    private void calculateCircleRadius() {
        mCircleRadius = mBubbleRadius - mDistance / 8;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        drawDebugInfo(canvas);
//        drawPoints(canvas);
        canvas.drawColor(Color.BLACK);
        if (mState != STATE_DISMISS) {
            drawBubble(canvas);
        }

        //只有在黏连状态下 才绘制连接
        if (mState == STATE_DRAG && isConnected()) {
            drawJoinPath(canvas);
            drawCircle(canvas);
        }

        if (mState == STATE_DISMISS && !mExplosionAnimEnd) {
            Log.d(TAG, "onDraw: 绘制爆炸");
            drawExplosion(canvas);
        }
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

    private void drawBubble(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(mBubbleCenter.x, mBubbleCenter.y, mBubbleRadius, mPaint);

    }

    private void drawCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(mCircleCenter.x, mCircleCenter.y, mCircleRadius, mPaint);
    }

    private void drawJoinPath(Canvas canvas) {
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

    private void drawExplosion(Canvas canvas) {
        mExplosionRect.set(
                (int) (mBubbleCenter.x - mBubbleRadius),
                (int) (mBubbleCenter.y - mBubbleRadius),
                (int) (mBubbleCenter.x + mBubbleRadius),
                (int) (mBubbleCenter.y + mBubbleRadius)
        );
        canvas.drawBitmap(mExplosionBitmaps[mCurExplosionPicIndex], null, mExplosionRect, mBitmapPaint);
    }
}
