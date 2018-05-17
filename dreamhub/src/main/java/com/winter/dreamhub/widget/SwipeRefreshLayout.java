package com.winter.dreamhub.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.google.common.base.Preconditions;
import com.winter.dreamhub.R;

/**
 * Created by hoaxoan on 10/24/2016.
 */

public class SwipeRefreshLayout extends android.support.v4.widget.SwipeRefreshLayout {
    private View noDataChild;
    private View scrollingChild;

    public SwipeRefreshLayout(Context context) {
        super(context);
    }

    public SwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*@Override
    public boolean canChildScrollUp() {
        if ((this.scrollingChild == null) || (this.noDataChild == null)) {
            this.scrollingChild = ((View) Preconditions.checkNotNull(findViewById(R.id.scrollable_view)));
            this.noDataChild = ((View) Preconditions.checkNotNull(findViewById(R.id.no_data_view)));
        }
        if (this.scrollingChild.getVisibility() == View.VISIBLE) {
            return ViewCompat.canScrollVertically(this.scrollingChild, -1);
        }
        return ViewCompat.canScrollVertically(this.noDataChild, -1);
    }*/
}
