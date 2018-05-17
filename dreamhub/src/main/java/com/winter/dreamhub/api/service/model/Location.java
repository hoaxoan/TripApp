package com.winter.dreamhub.api.service.model;

import java.math.BigInteger;

/**
 * Created by hoaxoan on 12/9/2016.
 */

public class Location {

    public Bounds bounds;
    public FeatureId featureId;
    public LatLng latlng;

    public Location() {

    }

    public Location(FeatureId featureId, LatLng latlng, Bounds bounds) {
        this.latlng = latlng;
        this.featureId = featureId;
        this.bounds = bounds;
    }

    public class Bounds {
        public Location.LatLng hi;
        public Location.LatLng lo;

        public Bounds(Location.LatLng lo, Location.LatLng hi) {
            this.lo = lo;
            this.hi = hi;
        }
    }

    public class FeatureId {
        public long cellId;
        public long fprint;

        public FeatureId(long cellId, long fprint) {
            this.cellId = cellId;
            this.fprint = fprint;
        }

        public FeatureId fromString(String featureIdStr) {
            String[] featureIds = featureIdStr.split(":");
            if (featureIds.length != 2) {
                return null;
            }
            try {
                return new FeatureId(new BigInteger(featureIds[0].substring(2), 16).longValue(), new BigInteger(featureIds[1].substring(2), 16).longValue());
            } catch (NumberFormatException ex) {
            }
            return null;
        }

        public final String toString() {
            return String.format("0x%s:0x%s", new Object[]{Long.toHexString(this.cellId), Long.toHexString(this.fprint)});
        }
    }

    public class LatLng {
        public int latE7;
        public int lngE7;

        public LatLng(int latE7, int lngE7) {
            this.latE7 = latE7;
            this.lngE7 = lngE7;
        }

        public LatLng fromString(String latLngStr) {
            String[] latLng = latLngStr.split(":");
            if (latLng.length != 2) {
                return null;
            }
            try {
                return new LatLng(new BigInteger(latLng[0].substring(2), 16).intValue(), new BigInteger(latLng[1].substring(2), 16).intValue());
            } catch (NumberFormatException ex) {
            }
            return null;
        }

        public final String toString() {
            return String.format("0x%s:0x%s", new Object[]{Integer.toHexString(this.latE7), Integer.toHexString(this.lngE7)});
        }
    }
}
