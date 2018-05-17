package com.winter.dreamhub.api.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.winter.dreamhub.api.model.BaseModel;

import java.security.acl.AclEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 12/11/2016.
 */

public class Hotel extends BaseModel implements Parcelable {

    public float prices;
    public float hotelClass;

    public List<HotelBooking> hotelBookings;

    public Hotel(long id,
                 String title,
                 float prices,
                 float hotelClass,
                 List<HotelBooking> hotelBookings) {
        super(id, title, null);
        this.prices = prices;
        this.hotelClass = hotelClass;
        this.hotelBookings = hotelBookings;
    }

    protected Hotel(Parcel in) {
        super(in.readLong(), in.readString(), in.readString());
        prices = in.readFloat();
        hotelClass = in.readFloat();

        if (in.readByte() == 0x01) {
            hotelBookings = new ArrayList<HotelBooking>();
            in.readList(hotelBookings, HotelBooking.class.getClassLoader());
        } else {
            hotelBookings = null;
        }
    }

    public static class Builder {
        private long id;
        private String title;
        private float prices;
        private float hotelClass;
        private List<HotelBooking> hotelBookings;

        public Hotel.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Hotel.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Hotel.Builder setPrices(float prices) {
            this.prices = prices;
            return this;
        }

        public Hotel.Builder setHotelClass(float hotelClass) {
            this.hotelClass = hotelClass;
            return this;
        }


        public Hotel.Builder setHotelBookings(List<HotelBooking> hotelBookings) {
            this.hotelBookings = hotelBookings;
            return this;
        }

        public Hotel build() {
            return new Hotel(id, title, prices, hotelClass, hotelBookings);
        }

        public static Hotel.Builder from(Hotel existing) {
            return new Hotel.Builder()
                    .setId(existing.id)
                    .setTitle(existing.title)
                    .setPrices(existing.prices)
                    .setHotelClass(existing.hotelClass)
                    .setHotelBookings(existing.hotelBookings);
        }
    }

    /* Parcelable stuff */

    @SuppressWarnings("unused")
    public static final Creator<Hotel> CREATOR = new Creator<Hotel>() {
        @Override
        public Hotel createFromParcel(Parcel in) {
            return new Hotel(in);
        }

        @Override
        public Hotel[] newArray(int size) {
            return new Hotel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeFloat(prices);
        dest.writeFloat(hotelClass);
        if (hotelBookings == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(hotelBookings);
        }
    }
}
