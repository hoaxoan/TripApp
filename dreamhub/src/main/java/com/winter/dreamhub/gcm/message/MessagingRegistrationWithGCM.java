package com.winter.dreamhub.gcm.message;

/**
 * Created by hoaxoan on 12/27/2016.
 */


import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.winter.dreamhub.gcm.GCMRegistrationIntentService;
import com.winter.dreamhub.gcm.GCMUnregisterIntentService;

import static com.winter.dreamhub.util.LogUtils.LOGE;
import static com.winter.dreamhub.util.LogUtils.LOGI;
import static com.winter.dreamhub.util.LogUtils.makeLogTag;

/**
 * Implements {@link MessagingRegistration} using GCM.
 */
public class MessagingRegistrationWithGCM implements MessagingRegistration {

    public static final String ACTIVE_ACCOUNT_NAME = "activeAccountName";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final String TAG = makeLogTag(MessagingRegistrationWithGCM.class);

    private Activity mActivity;

    public MessagingRegistrationWithGCM(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void registerDevice() {
        if (!checkPlayServices()) {
            LOGE(TAG, "GCM skipped. This device does not have Google Play Services which is " +
                    "required for GCM.");
            return;
        }

        Intent intent = new Intent(mActivity, GCMRegistrationIntentService.class);
        mActivity.startService(intent);
    }

    @Override
    public void unregisterDevice(String accountName) {
        Intent intent = new Intent(mActivity, GCMUnregisterIntentService.class);
        intent.putExtra(ACTIVE_ACCOUNT_NAME, accountName);
        mActivity.startService(intent);
    }

    @Override
    public void destroy() {
        // No operation needed here since GCMRegistrationIntentService and
        // GCMUnregisterIntentService are IntentServices they will stop themselves once their
        // onHandleIntent methods complete. If you are instead using a Service you should include
        // code here that stops the Service.
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If it doesn't, display a
     * dialog that allows users to download the APK from the Google Play Store or enable it in the
     * device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(mActivity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability
                        .getErrorDialog(mActivity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                LOGI(TAG, "Google Play Services is not available on this device.");
            }
            return false;
        }
        return true;
    }

}