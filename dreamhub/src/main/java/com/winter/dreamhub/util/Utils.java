package com.winter.dreamhub.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.winter.dreamhub.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hoaxoan on 5/1/2016.
 */
public class Utils {

    private static final String LRE;
    private static final String LRM;
    private static final String PDF;
    private static final String RLE;
    private static final String RLM;


    public static final Map<String, Integer> sColorsIndices;

    static {
        LRE = String.valueOf('?');
        RLE = String.valueOf('?');
        PDF = String.valueOf('?');
        LRM = String.valueOf('?');
        RLM = String.valueOf('?');
        sColorsIndices = new HashMap();
        sColorsIndices.put("b00006", Integer.valueOf(0));
        sColorsIndices.put("cc823f", Integer.valueOf(1));
        sColorsIndices.put("c9a421", Integer.valueOf(2));
        sColorsIndices.put("f09300", Integer.valueOf(3));
        sColorsIndices.put("47B98F", Integer.valueOf(4));
        sColorsIndices.put("13AAAF", Integer.valueOf(5));
        sColorsIndices.put("1E51BE", Integer.valueOf(6));

        sColorsIndices.put("7cb342", Integer.valueOf(7));
        sColorsIndices.put("28921f", Integer.valueOf(8));
        sColorsIndices.put("009688", Integer.valueOf(9));
        sColorsIndices.put("33b679", Integer.valueOf(10));
        sColorsIndices.put("039be5", Integer.valueOf(11));
        sColorsIndices.put("4285f4", Integer.valueOf(12));
        sColorsIndices.put("3f51b5", Integer.valueOf(13));
        sColorsIndices.put("7986cb", Integer.valueOf(14));
        sColorsIndices.put("b39ddb", Integer.valueOf(15));
        sColorsIndices.put("9e69af", Integer.valueOf(16));
        sColorsIndices.put("8e24aa", Integer.valueOf(17));
        sColorsIndices.put("ad1457", Integer.valueOf(18));
        sColorsIndices.put("d81b60", Integer.valueOf(19));
        sColorsIndices.put("e67c73", Integer.valueOf(20));
        sColorsIndices.put("795548", Integer.valueOf(21));
        sColorsIndices.put("616161", Integer.valueOf(22));
        sColorsIndices.put("a79b8e", Integer.valueOf(23));
        sColorsIndices.put("1454b9", Integer.valueOf(24));
    }

    public static void setAccessibilityIgnore(View view) {
        view.setClickable(false);
        view.setFocusable(false);
        view.setContentDescription("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }
    }

    public static Bitmap getRtlAdjustedImage(Context context, Bitmap bitmap) {
        if (!isLayoutDirectionRtl(context)) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0F, 1.0F);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        bitmap1.setDensity(160);
        return bitmap1;
    }

    public static boolean isLayoutDirectionRtl(Context context) {
        return context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    public static boolean isPortrait(Context context) {
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return true;
        }
        return false;
    }

    private static final int[] RES_IDS_ACTION_BAR_SIZE = {R.attr.actionBarSize};

    /**
     * Calculates the Action Bar height in pixels.
     */
    public static int calculateActionBarSize(Context context) {
        if (context == null) {
            return 0;
        }

        Resources.Theme curTheme = context.getTheme();
        if (curTheme == null) {
            return 0;
        }

        TypedArray att = curTheme.obtainStyledAttributes(RES_IDS_ACTION_BAR_SIZE);
        if (att == null) {
            return 0;
        }

        float size = att.getDimension(0, 0);
        att.recycle();
        return (int) size;
    }

    public static boolean isJellybeanMr2OrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean isKitKatOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean isLOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isMOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    public static boolean isAccessibilityEnabled(Context context) {
        if (context == null) {
            LogUtils.LOGD("CalUtils", "Null context passed to isAccessibilityEnabled");
            return false;
        }
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager == null || !accessibilityManager.isEnabled()) {
            return false;
        }
        if (!accessibilityManager.getEnabledAccessibilityServiceList(1).isEmpty()) {
        }
        return true;
    }

    public static void requestAccessibilityFocus(View view) {
        if (isAccessibilityEnabled(view.getContext())) {
            view.performAccessibilityAction(64, null);
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static void hideFocusAndSoftKeyboard(Activity context, View view) {
        if (context == null) {
            return;
        }
        View currentView = context.getCurrentFocus();
        if ((currentView != null) && (currentView != view)) {
            currentView.clearFocus();
        }
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public static CharSequence forceTextAlignment(CharSequence charSequence, boolean isFirst) {
        if (charSequence == null) {
            return null;
        }
        if (isFirst) {
            if (!TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR.isRtl(charSequence, 0, charSequence.length())) {
                TextUtils.concat(new CharSequence[]{RLM, TextUtils.concat(new CharSequence[]{RLE, charSequence, PDF}), RLM});
            }
        }
        return TextUtils.concat(new CharSequence[]{RLM, TextUtils.concat(new CharSequence[]{RLE, charSequence, PDF}), RLM});
    }

    public static String forceTextAlignment(String paramString, boolean paramBoolean) {
        return (String) forceTextAlignment(paramString, paramBoolean);
    }

    /*public static int getFirstDayOfWeekAsTime(Context context) {
        return Utils.convertDayOfWeekFromCalendarToTime(getFirstDayOfWeekAsCalendar(context));
    }*/

    public static int setVisibility(View view, boolean isShow) {
        if (view != null) {
            if (isShow) {
                view.setVisibility(View.VISIBLE);
                return 0;
            } else {
                view.setVisibility(View.GONE);
                return 8;
            }
        }
        return -1;
    }


    public static String formatTime(Calendar calendar) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(calendar.getTime());
    }

    public static void getListViewSize(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < adapter.getCount(); size++) {
            View listItem = adapter.getView(size, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
