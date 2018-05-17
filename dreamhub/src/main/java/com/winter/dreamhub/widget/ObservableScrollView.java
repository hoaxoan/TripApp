package com.winter.dreamhub.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by hoaxoan on 4/30/2016.
 */
public class ObservableScrollView extends ScrollView {
    private OnScrollChangeListener mListener;

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.mListener != null) {
            this.mListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollListener(OnScrollChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnScrollChangeListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}

