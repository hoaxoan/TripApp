package com.winter.dreamhub.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by hoaxoan on 11/2/2016.
 */

public class ToggleFrameLayout extends FrameLayout implements View.OnClickListener {
    public View firstView;
    public View secondView;
    private int visibleViewIndex = 0;

    public ToggleFrameLayout(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public ToggleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public ToggleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }

    private final boolean isViewsSet() {
        return (this.firstView != null) && (this.secondView != null);
    }

    private final void updateVisibility() {
        if (this.visibleViewIndex == 0) {
            this.firstView.setVisibility(View.VISIBLE);
            this.secondView.setVisibility(View.GONE);
        } else {
            this.secondView.setVisibility(View.VISIBLE);
            this.firstView.setVisibility(View.GONE);
        }
    }

    public void setVisibleViewIndex(int index){
        this.visibleViewIndex = index;
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        /*if (getChildCount() > 1) {
            throw new RuntimeException("The number of views [can't] be more than two.");
        }*/
        view.setClickable(false);
        if (getChildCount() == 0) {
            this.firstView = view;
        } else {
            this.secondView = view;
        }
        if (isViewsSet()) {
            updateVisibility();
        }
        super.addView(view, layoutParams);

    }

    @Override
    public void onClick(View v) {
        toggleViews();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.visibleViewIndex = savedState.visibleViewIndex;
        if (isViewsSet()) {
            updateVisibility();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.visibleViewIndex);
    }

    public final void toggleViews() {
        this.visibleViewIndex = ((this.visibleViewIndex + 1) % 2);
        updateVisibility();
    }

    static final class SavedState
            extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int visibleViewIndex;

        public SavedState(Parcel source) {
            super(source);
            this.visibleViewIndex = source.readInt();
        }

        public SavedState(Parcelable superState, int viewIndex) {
            super(superState);
            this.visibleViewIndex = viewIndex;
        }


        @Override
        public final void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.visibleViewIndex);
        }
    }

}
