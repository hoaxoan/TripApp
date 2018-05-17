package com.winter.dreamhub.gcm.command;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.winter.dreamhub.R;
import com.winter.dreamhub.activity.HomeActivity;
import com.winter.dreamhub.gcm.GCMCommand;

import static com.winter.dreamhub.util.LogUtils.LOGI;
import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Created by hoaxoan on 12/27/2016.
 */


public class AnnouncementCommand extends GCMCommand {
    private static final String TAG = makeLogTag("AnnouncementCommand");

    @Override
    public void execute(Context context, String type, String extraData) {
        LOGI(TAG, "Received GCM message: " + type);
        displayNotification(context, extraData);
    }

    private void displayNotification(Context context, String message) {
        LOGI(TAG, "Displaying notification: " + message);
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
                .notify(0, new NotificationCompat.Builder(context)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.drawable.ic_notification)
                        .setTicker(message)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(message)
                        //.setColor(context.getResources().getColor(R.color.theme_primary))
                        // Note: setColor() is available in the support lib v21+.
                        // We commented it out because we want the source to compile
                        // against support lib v20. If you are using support lib
                        // v21 or above on Android L, uncomment this line.
                        .setContentIntent(
                                PendingIntent.getActivity(context, 0,
                                        new Intent(context, HomeActivity.class)
                                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                        Intent.FLAG_ACTIVITY_SINGLE_TOP),
                                        0))
                        .setAutoCancel(true)
                        .build());
    }
}
