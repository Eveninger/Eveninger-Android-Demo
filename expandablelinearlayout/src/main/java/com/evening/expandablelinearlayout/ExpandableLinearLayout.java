package com.evening.expandablelinearlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Nighter on 17/7/20.
 * Source: http://blog.csdn.net/chay_chan/article/details/72810770
 */

public class ExpandableLinearLayout extends LinearLayout {
    private boolean mIsExpanded = false;
    private boolean mHasBottom;
    private int mDefaultItemCount = 2;
    private View mBottomView;

    public ExpandableLinearLayout(Context context) {
        this(context, null);
    }

    public ExpandableLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mBottomView = View.inflate(getContext(), R.layout.item_ell_bottom, null);
        mBottomView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    private void toggle() {
        if (mIsExpanded) {
            collapse();
        } else {
            expand();
        }
        mIsExpanded = !mIsExpanded;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        checkToAddBottom(getChildCount());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void addItem(View view) {
        int childCount = getChildCount();
        if (!mHasBottom) {
            addView(view);
        } else {
            addView(view, childCount - 2);
        }
        refreshUI(view);
    }

    private void refreshUI(View view) {
        int childCount = getChildCount();
        if (childCount > mDefaultItemCount) {
            if (childCount - mDefaultItemCount == 1) {
                checkToAddBottom(childCount);
            }
            view.setVisibility(GONE);
        }
    }

    //判断是否要添加底部
    private void checkToAddBottom(int childCount) {
        if (childCount > mDefaultItemCount && !mHasBottom) {
            addView(mBottomView);
            collapse();
            mHasBottom = true;
        }
    }

    private void collapse() {
        int endIndex = getChildCount() - 1;
        for (int i = mDefaultItemCount; i < endIndex; ++i) {
            View view = getChildAt(i);
            view.setVisibility(GONE);
        }
    }

    private void expand() {
        int childCount = getChildCount();
        for (int i = mDefaultItemCount; i < childCount; i++) {
            View view = getChildAt(i);
            view.setVisibility(VISIBLE);
        }
    }

    public interface OnStateChangeListener {
        void onStateChanged(boolean isExpanded);
    }
}
