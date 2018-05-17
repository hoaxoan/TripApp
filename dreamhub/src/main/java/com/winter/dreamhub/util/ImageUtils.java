/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.winter.dreamhub.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.view.Display;

import com.squareup.picasso.Picasso;

/**
 * Utility methods for working with images.
 */
public class ImageUtils {

    private Context context;
    public Picasso picasso;
    private DisplayUtils displayUtil;

    public ImageUtils(Context context, DisplayUtils displayUtil) {
        this.context = context;
        this.displayUtil = displayUtil;
    }

    public ImageUtils(Context context, Picasso picasso, DisplayUtils displayUtil) {
        this.context = context;
        this.picasso = picasso;
        this.displayUtil = displayUtil;
    }

    public static Bitmap vectorToBitmap(Context context, Drawable vector) {
        final Bitmap bitmap = Bitmap.createBitmap(vector.getIntrinsicWidth(),
                vector.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        vector.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vector.draw(canvas);
        return bitmap;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap vectorToBitmap(Context context, @DrawableRes int vectorDrawableId) {
        return vectorToBitmap(context, context.getDrawable(vectorDrawableId));
    }

    public final int getHeaderHeight() {
        Display display = this.displayUtil.windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return Math.round(point.x / getHeroImageAspectRatio());
    }

    public final float getHeroImageAspectRatio() {
        if (this.displayUtil.isPortrait()) {
            return 1.7777778F;
        }
        return 5.0F;
    }
}
