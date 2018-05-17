package com.winter.dreamhub.libs.service;

import com.winter.dreamhub.api.service.model.Trip;
import com.winter.dreamhub.libs.state.DownloadStateManager;

/**
 * Created by hoaxoan on 8/11/2017.
 */

public class DownloadHelper {

    TripDataHelper tripDataHelper;

    DownloadStateManager downloadStateManager;


    public final void cancelDownload(String accountId, String objectId, String destinationId) {
        this.downloadStateManager.cancelDownload(accountId, objectId, destinationId);
    }

    public final int getPercentComplete(String accountId, String objectId, String destinationId) {
        return this.downloadStateManager.getPercentComplete(accountId, objectId, destinationId);
    }

    public final boolean isCanceled(String accountId, String objectId, String destinationId) {
        return this.downloadStateManager.isCanceled(accountId, objectId, destinationId);
    }

    public final boolean isDownloading(String accountId, String objectId, String destinationId) {
        return this.downloadStateManager.isDownloading(accountId, objectId, destinationId);
    }

    public final void startDownload(String accountId, String objectId, String destinationId) {
        this.downloadStateManager.startDownload(accountId, objectId, destinationId);
    }

    public final void downloadSnippets(String accountId, String tripId, String destinationId) {
        Trip[] trips = new Trip[0];
        tripDataHelper.bulkInsertTrips(accountId, trips);
    }


    static MessageRequest buildRequest(String objectId) {
        MessageRequest request = new MessageRequest();
        return request;
    }

    public static final class MessageRequest {

    }
}
