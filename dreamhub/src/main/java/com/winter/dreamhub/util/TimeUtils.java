package com.winter.dreamhub.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.winter.dreamhub.BuildConfig;
import com.winter.dreamhub.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

import static com.winter.dreamhub.util.LogUtils.LOGW;
import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 9/5/2017.
 */

public class TimeUtils {
    public static final int SECOND = 1000;
    public static final int MINUTE = 60 * SECOND;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;

    private static final String TAG = makeLogTag(TimeUtils.class);

    private static final int FORMAT_SHORT_DATETIME_FLAGS = DateUtils.FORMAT_ABBREV_ALL
            | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_TIME;

    private static final SimpleDateFormat[] ACCEPTED_TIMESTAMP_FORMATS = {
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US),
            new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z", Locale.US)
    };

    private static final SimpleDateFormat VALID_IFMODIFIEDSINCE_FORMAT =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);

    public static Date parseTimestamp(String timestamp) {
        for (SimpleDateFormat format : ACCEPTED_TIMESTAMP_FORMATS) {
            // TODO: We shouldn't be forcing the time zone when parsing dates.
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                return format.parse(timestamp);
            } catch (ParseException ex) {
                continue;
            }
        }

        // All attempts to parse have failed
        return null;
    }

    public static boolean isValidFormatForIfModifiedSinceHeader(String timestamp) {
        try {
            return VALID_IFMODIFIEDSINCE_FORMAT.parse(timestamp) != null;
        } catch (Exception ex) {
            return false;
        }
    }

    public static long timestampToMillis(String timestamp, long defaultValue) {
        if (TextUtils.isEmpty(timestamp)) {
            return defaultValue;
        }
        Date d = parseTimestamp(timestamp);
        return d == null ? defaultValue : d.getTime();
    }

    /**
     * Format a {@code date} honoring the app preference for using Conference or device timezone.
     * {@code Context} is used to lookup the shared preference settings.
     */
    public static String formatShortDate(Context context, Date date) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        return DateUtils.formatDateRange(context, formatter, date.getTime(), date.getTime(),
                DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_NO_YEAR,
                SettingsUtils.getDisplayTimeZone(context).getID()).toString();
    }

    public static String formatShortTime(Context context, Date time) {
        // Android DateFormatter will honor the user's current settings.
        DateFormat format = android.text.format.DateFormat.getTimeFormat(context);
        // Override with Timezone based on settings since users can override their phone's timezone
        // with Pacific time zones.
        TimeZone tz = SettingsUtils.getDisplayTimeZone(context);
        if (tz != null) {
            format.setTimeZone(tz);
        }
        return format.format(time).toLowerCase(Locale.getDefault());
    }

    public static String formatShortTimeWithTimeZone(Context context, Date time, TimeZone timeZone) {
        // Android DateFormatter will honor the user's current settings.
        DateFormat format = android.text.format.DateFormat.getTimeFormat(context);
        // Override with Timezone based on settings since users can override their phone's timezone
        // with Pacific time zones.
        if (timeZone != null) {
            format.setTimeZone(timeZone);
        }
        return format.format(time).toLowerCase(Locale.getDefault());
    }

    public static String formatShortDateTime(Context context, Date date) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        String timezone = SettingsUtils.getDisplayTimeZone(context).getID();
        return DateUtils.formatDateRange(context, formatter, date.getTime(), date.getTime(),
                FORMAT_SHORT_DATETIME_FLAGS, timezone).toString().toUpperCase(Locale.getDefault());
    }

    public static String formatDuration(@NonNull Context context, long startTime, long endTime) {
        return formatDuration(context, endTime - startTime);
    }

    public static String formatDuration(@NonNull Context context, long duration) {
        Float hours = duration / (float) HOUR;
        if (hours >= 1f) {
            return context.getResources().getQuantityString(R.plurals.duration_hours,
                    (int) Math.ceil(hours), (hours == hours.intValue()) ?
                            String.valueOf(hours.intValue()) : hours.toString());
        } else {
            long mins = duration / MINUTE;
            return context.getResources().getQuantityString(
                    R.plurals.duration_minutes, (int) mins, mins);
        }
    }

    /**
     * Retrieve the current time. If the current build is a debug build, the mock time is returned
     * when set, taking into account the passage of time by adding the difference between the
     * current system time and the system time when the application was created.
     */
    public static long getCurrentTime(final Context context) {
        if (BuildConfig.DEBUG) {
            return context.getSharedPreferences(UIUtils.MOCK_DATA_PREFERENCES, Context.MODE_PRIVATE)
                    .getLong(UIUtils.PREFS_MOCK_CURRENT_TIME, System.currentTimeMillis())
                    + System.currentTimeMillis() - getAppStartTime(context);
        } else {
            return System.currentTimeMillis();
        }
    }

    /**
     * Set the current time only when the current build is a debug build.
     */
    private static void setCurrentTime(Context context, long newTime) {
        if (BuildConfig.DEBUG) {
            java.util.Date currentTime = new java.util.Date(TimeUtils.getCurrentTime(context));
            LOGW(TAG, "Setting time from " + currentTime + " to " + newTime);
            context.getSharedPreferences(UIUtils.MOCK_DATA_PREFERENCES, Context.MODE_PRIVATE).edit()
                    .putLong(UIUtils.PREFS_MOCK_CURRENT_TIME, newTime).apply();
        }
    }

    /**
     * Retrieve the app start time,set when the application was created. This is used to calculate
     * the current time, in debug mode only.
     */
    private static long getAppStartTime(final Context context) {
        return context.getSharedPreferences(UIUtils.MOCK_DATA_PREFERENCES, Context.MODE_PRIVATE)
                .getLong(UIUtils.PREFS_MOCK_APP_START_TIME, System.currentTimeMillis());
    }

    /**
     * Set the app start time only when the current build is a debug build.
     */
    public static void setAppStartTime(Context context, long newTime) {
        if (BuildConfig.DEBUG) {
            java.util.Date previousAppStartTime = new java.util.Date(TimeUtils.getAppStartTime(
                    context));
            LOGW(TAG, "Setting app startTime from " + previousAppStartTime + " to " + newTime);
            context.getSharedPreferences(UIUtils.MOCK_DATA_PREFERENCES, Context.MODE_PRIVATE).edit()
                    .putLong(UIUtils.PREFS_MOCK_APP_START_TIME, newTime).apply();
        }
    }
}
