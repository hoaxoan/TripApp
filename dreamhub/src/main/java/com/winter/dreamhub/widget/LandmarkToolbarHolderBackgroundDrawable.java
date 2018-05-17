package com.winter.dreamhub.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.winter.dreamhub.R;

/**
 * Created by hoaxoan on 10/30/2016.
 */

public class LandmarkToolbarHolderBackgroundDrawable extends Drawable {
    private final int maxUnderscorePaintHeight;
    int top;
    private final Paint topRectanglePaint = new Paint();
    private final Paint underscorePaint = new Paint();

    public LandmarkToolbarHolderBackgroundDrawable(Context context) {
        this.topRectanglePaint.setColor(context.getResources().getColor(R.color.quantum_white_100));
        this.underscorePaint.setColor(context.getResources().getColor(R.color.quantum_black_divider));
        this.maxUnderscorePaintHeight = context.getResources().getDimensionPixelSize(R.dimen.bottom_shadow_height);
    }

    @Override
    public void draw(Canvas canvas) {
        int width = getBounds().width();
        int height = Math.min(this.maxUnderscorePaintHeight, this.top);
        height = Math.max(this.top - height, 0);
        canvas.drawRect(new RectF(0.0F, 0.0F, width, height), this.topRectanglePaint);
        canvas.drawRect(new RectF(0.0F, height + 1, width, this.top), this.underscorePaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
