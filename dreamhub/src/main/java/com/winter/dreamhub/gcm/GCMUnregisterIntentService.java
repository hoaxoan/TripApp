package com.winter.dreamhub.gcm;


import android.app.IntentService;
import android.content.Intent;

import com.winter.dreamhub.gcm.message.MessagingRegistrationWithGCM;
import com.winter.dreamhub.util.AccountUtils;

import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Requests that the server remove the specified Instance ID token (gcmId) from being paired with
 * the user with the specified gcmKey. Then update shared preferences to indicate that
 * the application InstanceID token is not registered with the server.
 */
public class GCMUnregisterIntentService extends IntentService {

    private static final String TAG = makeLogTag(GCMUnregisterIntentService.class);

    public GCMUnregisterIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String accountName = intent
                .getStringExtra(MessagingRegistrationWithGCM.ACTIVE_ACCOUNT_NAME);
        // Get the correct GCM key for the user. GCM key is a somewhat non-standard
        // approach we use in this app. For more about this, check GCM.TXT.
        final String gcmKey = accountName != null ?
                AccountUtils.getGcmKey(this, accountName) : null;

        // Unregister on server.
        ServerUtilities.unregister(ServerUtilities.getGcmRegId(this), gcmKey);

        // Set device unregistered on server.
        ServerUtilities.setRegisteredOnServer(this, false, ServerUtilities.getGcmRegId(this), null);
    }
}