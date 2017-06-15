package com.evening.nestedscrolling.lmj121212;

import android.content.Context;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.support.v4.view.NestedScrollingParent;

/**
 * Created by Nighter on 17/5/23.
 */

public class NestedScrollParent extends LinearLayout implements NestedScrollingParent {
    private static final String TAG = "NestedScrollParent";
    private NestedScrollingParentHelper mParentHelper;

    public NestedScrollParent(Context context) {
        this(context, null);
    }

    public NestedScrollParent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        mParentHelper = new NestedScrollingParentHelper(this);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        mParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public void onStopNestedScroll(View child) {
        mParentHelper.onStopNestedScroll(child);
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        consumed[1] = dy;
    }

}
