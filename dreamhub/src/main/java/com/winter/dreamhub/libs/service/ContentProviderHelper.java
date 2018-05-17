package com.winter.dreamhub.libs.service;

import android.net.Uri;

/**
 * Created by hoaxoan on 8/11/2017.
 */

public final class ContentProviderHelper {
    public static Uri appendAccountDestinationIdParameter(Uri uri, String accountId, String destinationId) {
        return uri.buildUpon()
                .appendQueryParameter("account_id", accountId)
                .appendQueryParameter("destination_id", destinationId)
                .build();
    }

    public static Uri appendAccountIdParameter(Uri uri, String accountId) {
        return uri.buildUpon()
                .appendQueryParameter("account_id", accountId)
                .build();
    }
}
