package com.winter.dreamhub.util;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by hoaxoan on 10/26/2016.
 */

public class DisplayUtils {

    public WindowManager windowManager;

    public DisplayUtils(WindowManager windowManager){
        this.windowManager = windowManager;
    }

    @TargetApi(13)
    public final int getHeight() {
        Display localDisplay = this.windowManager.getDefaultDisplay();
        Point localPoint = new Point();
        localDisplay.getSize(localPoint);
        return localPoint.y;
    }

    @TargetApi(13)
    public final boolean isPortrait() {
        Display localDisplay = this.windowManager.getDefaultDisplay();
        Point localPoint = new Point();
        localDisplay.getSize(localPoint);
        return localPoint.x < localPoint.y;
    }
}
