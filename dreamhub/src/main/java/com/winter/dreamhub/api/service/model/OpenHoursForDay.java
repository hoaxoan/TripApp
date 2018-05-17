package com.winter.dreamhub.api.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;


/**
 * Created by hoaxoan on 12/4/2016.
 */

public class OpenHoursForDay implements Parcelable {
    public final int id;
    public final String name;
    public final Date date;
    public final Float startHour;
    public final Float endHour;

    public OpenHoursForDay(int id,
                           String name,
                           Date date,
                           Float startHour,
                           Float endHour) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    protected OpenHoursForDay(Parcel in) {
        id = in.readInt();
        name = in.readString();
        date = (Date) in.readSerializable();
        startHour = in.readFloat();
        endHour = in.readFloat();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeSerializable(date);
        dest.writeFloat(startHour);
        dest.writeFloat(endHour);
    }

    @SuppressWarnings("unused")
    public static final Creator<OpenHoursForDay> CREATOR = new Creator<OpenHoursForDay>() {
        @Override
        public OpenHoursForDay createFromParcel(Parcel in) {
            return new OpenHoursForDay(in);
        }

        @Override
        public OpenHoursForDay[] newArray(int size) {
            return new OpenHoursForDay[size];
        }
    };

}
