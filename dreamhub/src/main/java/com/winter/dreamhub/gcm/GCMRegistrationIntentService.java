package com.winter.dreamhub.gcm;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.winter.dreamhub.BuildConfig;
import com.winter.dreamhub.util.AccountUtils;

import java.io.IOException;

import static com.winter.dreamhub.util.LogUtils.LOGE;
import static com.winter.dreamhub.util.LogUtils.LOGI;
import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * {@link android.app.IntentService} responsible for handling GCM messages.
 */
public class GCMRegistrationIntentService extends IntentService {

    private static final String TAG = makeLogTag("GCMRegistrationIntentService");

    private static final String CONFERENCE_MESSAGES_TOPIC_ONSITE = "/topics/confmessagesonsite";
    private static final String CONFERENCE_MESSAGES_TOPIC_OFFSITE = "/topics/confmessagesoffsite";

    public GCMRegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(BuildConfig.GCM_SENDER_ID,
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            // If user account is active send token and gcmKey to server. This enables the server
            // to map the user's identifier with the applications's InstanceID token, with this the
            // server can send user specific messages.
            if (AccountUtils.hasActiveAccount(this)) {
                // Get the correct GCM key for the user. GCM key is a somewhat non-standard
                // approach we use in this app. For more about this, check GCM.TXT.
                final String gcmKey = AccountUtils.hasActiveAccount(this) ?
                        AccountUtils.getGcmKey(this, AccountUtils.getActiveAccountName(this)) :
                        null;

                sendRegistrationToServer(token, gcmKey);
            }
            subscribeTopics(token);
        } catch (IOException e) {
            LOGE(TAG, "An exception occurred generating InstanceID token: " + e.getMessage());
        }
    }

    /**
     * Send the generated InstanceID token to the server to be paired with the user identifying
     * gcmKey.
     *
     * @param token  InstanceID token that GCM uses to send messages to this application instance.
     * @param gcmKey String used to pair a user with an InstanceID token.
     */
    private void sendRegistrationToServer(String token, String gcmKey) {
        if (!ServerUtilities.isRegisteredOnServer(this, gcmKey)) {
            LOGI(TAG, "Registering on the GCM server with GCM key: " + gcmKey);
            boolean registered = ServerUtilities.register(this, token, gcmKey);

            if (!registered) {
                // At this point all attempts to register with the app
                // server failed, the app will try to register again when
                // it is restarted.
                LOGI(TAG, "GCM registration failed.");
            } else {
                LOGI(TAG, "GCM registration successful.");
                ServerUtilities.setRegisteredOnServer(this, true, token, gcmKey);
            }
        } else {
            LOGI(TAG, "Already registered on the GCM server with GCM key " + gcmKey);
        }
    }

    private void subscribeTopics(String registrationToken) {
        try {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.unsubscribe(registrationToken, CONFERENCE_MESSAGES_TOPIC_ONSITE);
            pubSub.unsubscribe(registrationToken, CONFERENCE_MESSAGES_TOPIC_OFFSITE);
        } catch (Throwable throwable) {
            // Just in case.
            LOGE(TAG, "Exception updating conference message cards subscription.", throwable);
        }
    }
}
