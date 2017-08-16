package com.evening.behavior.behavior;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.evening.behavior.R;

/**
 * Created by Nighter on 17/8/14.
 */

public class FollowBehavior extends CoordinatorLayout.Behavior {
    private int mTargetId;

    public FollowBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FollowBehavior);
        mTargetId = array.getResourceId(R.styleable.FollowBehavior_target, -1);
        array.recycle();
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setY(dependency.getY() + dependency.getHeight());
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency.getId() == mTargetId;
    }
}
