package com.winter.dreamhub.util;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * Created by hoaxoan on 10/29/2016.
 */

public class TypefaceSpan extends MetricAffectingSpan {
    private final Typeface typeface;

    public TypefaceSpan(Typeface typeface) {
        this.typeface = typeface;
    }

    private final void apply(Paint paint) {
        paint.setTypeface(this.typeface);
    }

    @Override
    public final void updateDrawState(TextPaint textPaint) {
        apply(textPaint);
    }

    @Override
    public final void updateMeasureState(TextPaint textPaint) {
        apply(textPaint);
    }
}
