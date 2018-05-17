package com.winter.dreamhub.util;

import com.winter.dreamhub.api.service.model.Location;

/**
 * Created by hoaxoan on 3/12/2017.
 */

public class MercatorProjection {
    private static final double LN2 = Math.log(2.0D);

    public static int exactZoom(Location.Bounds bounds, int paramInt1, int paramInt2) {
        int i = bounds.hi.latE7;
        new StringBuilder(25).append("Bounds.Hi.lat ").append(i);
        i = bounds.hi.lngE7;
        new StringBuilder(25).append("Bounds.Hi.lng ").append(i);
        i = bounds.lo.latE7;
        new StringBuilder(25).append("Bounds.Lo.lat ").append(i);
        i = bounds.lo.lngE7;
        new StringBuilder(25).append("Bounds.Lo.lng ").append(i);
        double d3 = lngToPixelAtZoom0(bounds.lo.lngE7 / 1.0E7D);
        double d2 = lngToPixelAtZoom0(bounds.hi.lngE7 / 1.0E7D);
        double d1 = d2;
        if (d3 > d2) {
            d1 = d2 + 256.0D;
        }
        d1 = Math.log(paramInt1 / (d1 - d3)) / LN2;
        d2 = Math.abs(latToPixelAtZoom0(bounds.hi.latE7 / 1.0E7D) - latToPixelAtZoom0(bounds.lo.latE7 / 1.0E7D));
        d2 = Math.log(paramInt2 / d2) / LN2;
        new StringBuilder(38).append("width: ").append(paramInt1).append(" height: ").append(paramInt2);
        new StringBuilder(73).append("Calculated zoom levels: ").append(d1).append(" ").append(d2);
        return (int) Math.floor(Math.min(d1, d2));
    }

    private static double latToPixelAtZoom0(double lat) {
        double latToPixel = Math.sin(Math.min(1.48442222974533D, Math.max(-1.48442222974533D, 0.017453292519943295D * lat)));
        return (Math.log((latToPixel + 1.0D) / (1.0D - latToPixel)) * 0.5D / 3.141592653589793D + 1.0D) * 0.5D * 256.0D;
    }

    private static double lngToPixelAtZoom0(double lng) {
        return (0.017453292519943295D * lng / 3.141592653589793D + 1.0D) * 0.5D * 256.0D;
    }
}
