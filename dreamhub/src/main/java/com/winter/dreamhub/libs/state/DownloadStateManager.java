package com.winter.dreamhub.libs.state;

import android.content.Context;
import android.content.SharedPreferences;

import com.winter.dreamhub.util.EncodingUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 8/11/2017.
 */

public class DownloadStateManager {

    private final Context context;

    public DownloadStateManager(Context context) {
        this.context = context;
    }

    private final SharedPreferences getSharedPreferences(String accountId, String objectId, String destinationId) {
        String source = String.format("%s%s%s", new Object[]{objectId, "_", destinationId.toString()});

        String accountStr = String.valueOf("preferences.account.");

        String prefix = String.valueOf(destinationId).length() + 0 + String.valueOf(accountStr).length() + accountId;

        String s = EncodingUtils.encodeWithPrefix(prefix, source);
        return this.context.getSharedPreferences(s, 0);
    }

    public final void cancelDownload(String accountId, String objectId, String destinationId) {
    }


    public final void clear(String accountId, String objectId, String destinationId) {
    }

    public final List<DownloadInfo> getDownloadsInProgress() {
        List<DownloadInfo> downloadInfos = new ArrayList();
        return downloadInfos;
    }

    public final int getPercentComplete(String accountId, String objectId, String destinationId) {
        return 0;
    }

    public final long getTimestamp(String accountId, String objectId, String destinationId) {
        return 0l;
    }

    public final boolean isCanceled(String accountId, String objectId, String destinationId) {
        return false;
    }

    public final boolean isDownloading(String accountId, String objectId, String destinationId) {
        return false;
    }

    public final void setPercentComplete(String accountId, String objectId, String destinationId, int percent) {
    }

    public final void startDownload(String accountId, String objectId, String destinationId) {
    }

    public static final class DownloadInfo {
        public final String accountId;
        public final String destinationId;
        public final String objectId;

        DownloadInfo(String accountId, String objectId, String destinationId) {
            this.accountId = accountId;
            this.objectId = objectId;
            this.destinationId = destinationId;
        }
    }
}
