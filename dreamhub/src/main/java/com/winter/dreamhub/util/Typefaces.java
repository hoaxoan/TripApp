package com.winter.dreamhub.util;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Build.VERSION;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoaxoan on 10/29/2016.
 */

public class Typefaces {
    private static final TypefaceDescriptor ROBOTO_CONDENSED_DESCRIPTOR;
    private static final TypefaceDescriptor ROBOTO_MEDIUM_DESCRIPTOR;
    private static final TypefaceDescriptor ROBOTO_REGULAR_DESCRIPTOR = new TypefaceDescriptor(14, "sans-serif", null);
    private static final Map<String, TypefaceDescriptor> descriptors;
    private static final Map<String, Typeface> typefacesCache;

    static {
        ROBOTO_MEDIUM_DESCRIPTOR = new TypefaceDescriptor(21, "sans-serif-medium", "fonts/Roboto-Medium.ttf");
        ROBOTO_CONDENSED_DESCRIPTOR = new TypefaceDescriptor(16, "sans-serif-condensed", null);
        descriptors = new HashMap();
        typefacesCache = new HashMap();
        descriptors.put("ROBOTO_MEDIUM", ROBOTO_MEDIUM_DESCRIPTOR);
        descriptors.put("ROBOTO_CONDENSED", ROBOTO_CONDENSED_DESCRIPTOR);
        descriptors.put("ROBOTO_REGULAR", ROBOTO_REGULAR_DESCRIPTOR);
    }

    public static Typeface getTypeface(Context context, String key, int style) {
        TypefaceDescriptor descriptor = (TypefaceDescriptor) descriptors.get(key);
        Typeface typeface = null;
        if (descriptor != ROBOTO_MEDIUM_DESCRIPTOR) {
            typeface = Typeface.create(descriptor.fontFamilyName, style);
        } else {
            typeface = Typeface.createFromAsset(context.getAssets(), descriptor.fileName);
        }

        if (!typefacesCache.containsKey(key)) {
            typefacesCache.put(key, typeface);
        }
        return (Typeface) typefacesCache.get(key);
    }

    private static final class TypefaceDescriptor {
        String fileName;
        String fontFamilyName;
        int minApiLevel;

        TypefaceDescriptor(int minApiLevel, String fontFamilyName, String fileName) {
            this.minApiLevel = minApiLevel;
            this.fontFamilyName = fontFamilyName;
            this.fileName = fileName;
        }
    }
}
