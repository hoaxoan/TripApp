package com.winter.dreamhub.widget.transitions;

import android.content.Context;
import android.graphics.Rect;
import android.transition.ChangeBounds;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.View;

/**
 * Shared element transitions do not seem to like transitioning from a single view to two separate
 * views so we need to alter the ChangeBounds transition to compensate
 */
public class StoryTitleSharedEnter extends ChangeBounds {

    private static final String PROPNAME_BOUNDS = "android:changeBounds:bounds";
    private static final String PROPNAME_PARENT = "android:changeBounds:parent";

    public StoryTitleSharedEnter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        Rect bounds = (Rect) transitionValues.values.get(PROPNAME_BOUNDS);
        bounds.right = ((View) transitionValues.values.get(PROPNAME_PARENT)).getWidth();
        bounds.bottom = ((View) transitionValues.values.get(PROPNAME_PARENT)).getHeight();
        transitionValues.values.put(PROPNAME_BOUNDS, bounds);
    }
}
