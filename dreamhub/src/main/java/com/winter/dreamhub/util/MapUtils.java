package com.winter.dreamhub.util;

import android.net.Uri;

import com.winter.dreamhub.api.service.model.Location;

import java.net.URI;
import java.util.Locale;

/**
 * Created by hoaxoan on 3/12/2017.
 */

public class MapUtils {
    int displayDensityDpi;
    int displayPortraitWidth;

    public static String format(String format, Object... args) {
        return String.format(Locale.US, format, args);
    }

    private final int getScale() {
        int scale = 1;
        if (this.displayDensityDpi > 160) {
            scale = 2;
        }
        if (this.displayDensityDpi > 480) {
            scale = 4;
        }
        return scale;
    }

    public final Uri getMapIntentUri(Location location) {
        if (location.featureId != null) {
            String cid = location.featureId.toString();
            Uri uri = Uri.parse("https://maps.google.com/maps")
                    .buildUpon()
                    .appendQueryParameter("cid", cid)
                    .build();
            return uri;
        }
        if (location.latlng == null) {
            return null;
        }
        int lat = location.latlng.latE7;
        int lng = location.latlng.lngE7;
        return Uri.parse("https://maps.google.com/maps")
                .buildUpon()
                .appendQueryParameter("q", format("%.6f,%.6f",
                        new Object[]{Double.valueOf(lat / 1.0E7D), Double.valueOf(lng / 1.0E7D)}))
                .build();
    }

    public final Uri getMapIntentUri(int lat, int lng) {
        return Uri.parse("https://maps.google.com/maps")
                .buildUpon()
                .appendQueryParameter("q", format("%.6f,%.6f",
                        new Object[]{Double.valueOf(lat / 1.0E7D), Double.valueOf(lng / 1.0E7D)}))
                .build();
    }


    public final String getMapUrlForLocation(Location location) {
        int displayPortraitWidth = this.displayPortraitWidth;
        int scale = getScale();
        int ratio = (int) (this.displayPortraitWidth / 1.7777778F);
        int zoom = 15;
        if (location != null) {
            if (location.bounds != null) {
                zoom = Math.min(15, MercatorProjection.exactZoom(location.bounds, displayPortraitWidth / scale, ratio / scale));
            }
        }
        return getMapUrlForLocation(location, zoom);
    }

    public final String getMapUrlForLocation(Location location, int zoom) {
        if ((location == null) || (location.latlng == null)) {
            return null;
        }
        double lat = location.latlng.latE7 / 1.0E7D;
        double lng = location.latlng.lngE7 / 1.0E7D;
        int displayPortraitWidth = this.displayPortraitWidth;
        int scale = getScale();
        int ratio = (int) (displayPortraitWidth / 1.7777778F);
        return format("https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=%s&format=png&size=%s&markers=color:red%%7C%.6f,%.6f&scale=%d&key=AIzaSyDpGfzfz6bYWHEOCAgM-nMYuo58ho7y0E4",
                new Object[]{Double.valueOf(lat), Double.valueOf(lng), Integer.valueOf(zoom),
                        String.format("%dx%d", new Object[]{Integer.valueOf(displayPortraitWidth / scale), Integer.valueOf(ratio / scale)}),
                        Double.valueOf(lat), Double.valueOf(lng), Integer.valueOf(getScale())});
    }
}
