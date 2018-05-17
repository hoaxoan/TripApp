package com.winter.dreamhub.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DisablableViewPager
        extends LayoutDirectionAwareViewPager {
    private boolean mHasScrolledLeft;
    private boolean mHasScrolledRight;
    private float mInitialX;
    private float mLastMotionX;
    public boolean mLeftScrollEnabled = true;
    public boolean mRightScrollEnabled = true;

    public DisablableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean swipeDirectionEnabled(MotionEvent ev) {
        if (ev.getAction() == 0) {
            this.mInitialX = ev.getX();
            this.mHasScrolledLeft = false;
            this.mHasScrolledRight = false;
            return true;
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if ((isEnabled()) && (swipeDirectionEnabled(ev))) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if ((isEnabled()) && (swipeDirectionEnabled(ev))) {
            try {
                return super.onTouchEvent(ev);
            } catch (IllegalArgumentException ex) {
            }
        }
        return false;
    }
}
