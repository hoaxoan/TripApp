package com.winter.dreamhub.api.service.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hoaxoan on 12/6/2016.
 */

public class HotelBooking implements Parcelable {
    public final long id;
    public final String name;
    public final String webUrl;
    public final Float prices;
    public final String unit;

    public HotelBooking(long id,
                        String name,
                        String webUrl,
                        Float prices,
                        String unit) {
        this.id = id;
        this.name = name;
        this.webUrl = webUrl;
        this.prices = prices;
        this.unit = unit;
    }

    protected HotelBooking(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.webUrl = in.readString();
        this.prices = in.readFloat();
        this.unit = in.readString();
    }

    public static final Creator<HotelBooking> CREATOR = new Creator<HotelBooking>() {
        @Override
        public HotelBooking createFromParcel(Parcel in) {
            return new HotelBooking(in);
        }

        @Override
        public HotelBooking[] newArray(int size) {
            return new HotelBooking[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(webUrl);
        parcel.writeFloat(prices);
        parcel.writeString(unit);
    }

    public static class Builder {
        private long id;
        private String name;
        private String webUrl;
        private Float prices;
        private String unit;

        public HotelBooking.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public HotelBooking.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public HotelBooking.Builder setWebUrl(String webUrl) {
            this.webUrl = webUrl;
            return this;
        }

        public HotelBooking.Builder setPrices(Float prices) {
            this.prices = prices;
            return this;
        }

        public HotelBooking.Builder setUnit(String unit) {
            this.unit = unit;
            return this;
        }

        public HotelBooking build() {
            return new HotelBooking(id, name, webUrl, prices, unit);
        }

        public static HotelBooking.Builder from(HotelBooking existing) {
            return new HotelBooking.Builder()
                    .setId(existing.id)
                    .setName(existing.name)
                    .setWebUrl(existing.webUrl)
                    .setPrices(existing.prices)
                    .setUnit(existing.unit);
        }
    }

}
