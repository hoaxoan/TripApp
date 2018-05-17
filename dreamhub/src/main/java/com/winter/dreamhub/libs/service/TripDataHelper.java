package com.winter.dreamhub.libs.service;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.winter.dreamhub.api.service.model.Trip;

/**
 * Created by hoaxoan on 8/11/2017.
 */

public class TripDataHelper {

    public final ContentResolver contentResolver;

    public TripDataHelper(Context context) {
        this.contentResolver = context.getContentResolver();
    }

    private static ContentValues prepareContentValues(Trip trip) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", trip.id);
        contentValues.put("TripName", trip.name);
        return contentValues;
    }

    public final boolean bulkInsertTrips(String accountId, Trip[] trips) {
        Uri uri = ContentProviderHelper.appendAccountIdParameter(TripStore.TRIPS_URI, accountId);

        ContentValues[] contentValues = new ContentValues[trips.length];
        for (int i = 0; i < trips.length; i++) {
            contentValues[i] = prepareContentValues(trips[i]);
        }
        int effectRows = this.contentResolver.bulkInsert(uri, contentValues);
        return effectRows == trips.length ? true : false;
    }


    public final boolean deleteTrip(String accountId, String id) {
        Uri uri = ContentProviderHelper.appendAccountIdParameter(TripStore.TRIPS_URI, accountId);
        return this.contentResolver.delete(uri, "_id=?", new String[]{id}) == 1;
    }


}
