package com.winter.dreamhub.util;

import android.text.Spannable;
import android.text.Spanned;

/**
 * Created by hoaxoan on 8/20/2016.
 */
public class SpanUtils {
    public static <T> T[] getAllSpansOfType(Spanned spanned, Class<T> type) {
        return spanned.getSpans(0, spanned.length(), type);
    }

    public static <T> T getLastSpanOfType(Spanned spanned, Class<T> type) {
        T[] spans = getAllSpansOfType(spanned, type);
        if (spans.length > 0) {
            return spans[(spans.length - 1)];
        }
        return null;
    }

    public static <T> T getSpanAt(Spanned spanned, int position, Class<T> type) {
        T[] spans = spanned.getSpans(position, position, type);
        if (spans.length > 0) {
            return spans[0];
        }
        return null;
    }

    public static void setSpanEndToEnd(Spannable spannable, Object what) {
        spannable.setSpan(what, 0, spannable.length(), 18);
    }
}
