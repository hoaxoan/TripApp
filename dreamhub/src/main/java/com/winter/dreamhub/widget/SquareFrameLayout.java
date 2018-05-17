package com.winter.dreamhub.widget;

/**
 * Created by hoaxoan on 11/12/2016.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * A square {@link FrameLayout}.
 */
public class SquareFrameLayout extends FrameLayout {

    public SquareFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Enforce a square tile by passing width MS to both params.
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}