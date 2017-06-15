package com.evening.scroller;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by Nighter on 17/5/25.
 */

public class ScrollerLayout extends ViewGroup {
    private static final String TAG = "ScrollerLayout";

    private Scroller mScroller;
    private int mTouchSlop;
    private float mXDown;
    private float mXMove;
    private float mXLastMove;
    private int mLeftBorder;
    private int mRightBorder;
    private VelocityTracker mVelocityTracker;

    public ScrollerLayout(Context context) {
        this(context, null);
    }

    public ScrollerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledPagingTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            // 为ScrollerLayout中的每一个子控件测量大小
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                // 为ScrollerLayout中的每一个子控件在水平方向上进行布局
                childView.layout(i * childView.getMeasuredWidth(), 0, (i + 1) * childView.getMeasuredWidth(), childView.getMeasuredHeight());
            }
            // 初始化左右边界值
            mLeftBorder = getChildAt(0).getLeft();
            mRightBorder = getChildAt(getChildCount() - 1).getRight();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mXDown = ev.getRawX();
                mXLastMove = mXDown;
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = ev.getRawX();
                float diff = Math.abs(mXMove - mXDown);
                mXLastMove = mXMove;
                // 当手指拖动值大于TouchSlop值时，认为应该进行滚动，拦截子控件的事件
                if (diff > mTouchSlop) {
                    //拦截时间
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mXMove = event.getRawX();
                int scrolledX = (int) (mXLastMove - mXMove);
                Log.e(TAG, "onTouchEvent: scrolledX = " + scrolledX);
                if (getScrollX() + scrolledX < mLeftBorder) {
                    scrollTo(mLeftBorder, 0);
                    return true;
                } else if (getScrollX() + getWidth() + scrolledX > mRightBorder) {
                    scrollTo(mRightBorder - getWidth(), 0);
                    return true;
                }
//                scrollBy(scrolledX, 0);
                overScrollBy(scrolledX, 0, getScrollX(), 0, mRightBorder + 50, 0, mRightBorder + 50, 0, true);
                mXLastMove = mXMove;
                break;
            case MotionEvent.ACTION_UP:
                // 当手指抬起时，根据当前的滚动值来判定应该滚动到哪个子控件的界面
                int targetIndex = (getScrollX() + getWidth() / 2) / getWidth();
                int dx = targetIndex * getWidth() - getScrollX();
                // 第二步，调用startScroll()方法来初始化滚动数据并刷新界面
                mVelocityTracker.computeCurrentVelocity(1000);
                mScroller.fling(getScrollX(), 0, -((int) mVelocityTracker.getXVelocity()), 0, 0, Integer.MAX_VALUE, 0, 0);
                mVelocityTracker.recycle();
                mVelocityTracker = null;
//                mScroller.startScroll(getScrollX(), 0, dx, 0);

                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (mScroller.computeScrollOffset()) {
            if (mScroller.getCurrX() + getWidth() > mRightBorder) {
                scrollTo(mRightBorder - getWidth(), 0);
            } else if (mScroller.getCurrX() < mLeftBorder) {
                scrollTo(mLeftBorder, 0);
            } else {
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            }
            invalidate();
        }
    }
}






















