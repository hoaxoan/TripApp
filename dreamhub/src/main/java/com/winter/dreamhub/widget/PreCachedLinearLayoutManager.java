package com.winter.dreamhub.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.winter.dreamhub.util.DisplayUtils;

/**
 * Created by hoaxoan on 10/28/2016.
 */

public class PreCachedLinearLayoutManager extends LinearLayoutManager {
    private DisplayUtils displayUtil;

    public PreCachedLinearLayoutManager(Context context, int orientation, boolean reverseLayout, DisplayUtils displayUtil) {
        super(context, orientation, reverseLayout);
        this.displayUtil = displayUtil;
    }

    public final int getExtraLayoutSpace(RecyclerView.State state) {
        return this.displayUtil.getHeight();
    }
}
