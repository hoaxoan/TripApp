package com.winter.dreamhub.api.service.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hoaxoan on 12/6/2016.
 */

public class VisitedPlace implements Parcelable {
    public final long id;
    public final long user_id;
    public final long related_id;
    public final String related_type;
    public final String field;

    public VisitedPlace(long id,
                        long user_id,
                        long related_id,
                        String related_type,
                        String field) {
        this.id = id;
        this.user_id = user_id;
        this.related_id = related_id;
        this.related_type = related_type;
        this.field = field;
    }

    protected VisitedPlace(Parcel in) {
        this.id = in.readLong();
        this.user_id = in.readLong();
        this.related_id = in.readLong();
        this.related_type = in.readString();
        this.field = in.readString();
    }

    public static final Creator<VisitedPlace> CREATOR = new Creator<VisitedPlace>() {
        @Override
        public VisitedPlace createFromParcel(Parcel in) {
            return new VisitedPlace(in);
        }

        @Override
        public VisitedPlace[] newArray(int size) {
            return new VisitedPlace[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(user_id);
        parcel.writeLong(related_id);
        parcel.writeString(related_type);
        parcel.writeString(field);
    }

}
