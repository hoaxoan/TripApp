package com.winter.dreamhub.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;
import com.winter.dreamhub.util.AccountUtils;

import static com.winter.dreamhub.util.LogUtils.LOGV;
import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * In the event that the current InstanceID token is invalidated we request the token be unpaired
 * with the user. Then {@see com.google.samples.apps.iosched.gcm.GCMRegistrationIntentService} is
 * started to generate a new InstanceID token.
 */
public class GCMInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = makeLogTag("GCMIIDListenerService");

    @Override
    public void onTokenRefresh() {
        LOGV(TAG, "Set registered to false");
        ServerUtilities.setRegisteredOnServer(this, false, ServerUtilities.getGcmRegId(this), null);

        // Get the correct GCM key for the user. GCM key is a somewhat non-standard
        // approach we use in this app. For more about this, check GCM.md.
        final String gcmKey = AccountUtils.hasActiveAccount(this) ?
                AccountUtils.getGcmKey(this, AccountUtils.getActiveAccountName(this)) : null;

        // Unregister on server.
        ServerUtilities.unregister(ServerUtilities.getGcmRegId(this), gcmKey);

        // Register for a new InstanceID token. This token is sent to the server to be paired with
        // the current user's GCM key.
        Intent intent = new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
    }
}

