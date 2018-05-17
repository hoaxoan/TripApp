package com.winter.dreamhub.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.WindowInsets;
import android.widget.FrameLayout;

/**
 * Created by hoaxoan on 10/24/2016.
 */

public class WindowFitFrameLayout
        extends FrameLayout {
    public WindowFitFrameLayout(Context context) {
        super(context);
        setFitsSystemWindows(true);
    }

    public WindowFitFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFitsSystemWindows(true);
    }

    public WindowFitFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFitsSystemWindows(true);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if (Build.VERSION.SDK_INT >= 21) {
            setPadding(getPaddingLeft(), insets.getSystemWindowInsetTop(), getPaddingRight(), getPaddingBottom());
        }
        return super.onApplyWindowInsets(insets);
    }
}
