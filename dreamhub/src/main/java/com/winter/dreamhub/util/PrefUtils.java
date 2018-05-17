package com.winter.dreamhub.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by hoaxoan on 6/11/2016.
 */
public class PrefUtils {
    private static final String PREF_NAME = "pref_dream_hub_onboard_welcome";
    private static final String IS_FIRST_TIME_LAUNCH = "pref_dream_hub_first_time_launch";

    public static void markFirstInstall(final Context context, final boolean installed) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(IS_FIRST_TIME_LAUNCH, installed).apply();
    }

    public static boolean hasFistInstall(final Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }


}
