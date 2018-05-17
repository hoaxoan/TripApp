package com.winter.dreamhub.api.service.request;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hoaxoan on 12/6/2016.
 */

public class VisitedPlacesRequest implements Parcelable {
    public final long trip_id;
    public final long user_id;
    public final long related_id;
    public final String related_type;
    public final String field;

    public VisitedPlacesRequest(long trip_id,
                                long user_id,
                                long related_id,
                                String related_type,
                                String field) {
        this.trip_id = trip_id;
        this.user_id = user_id;
        this.related_id = related_id;
        this.related_type = related_type;
        this.field = field;
    }

    protected VisitedPlacesRequest(Parcel in) {
        this.trip_id = in.readLong();
        this.user_id = in.readLong();
        this.related_id = in.readLong();
        this.related_type = in.readString();
        this.field = in.readString();
    }

    public static final Creator<VisitedPlacesRequest> CREATOR = new Creator<VisitedPlacesRequest>() {
        @Override
        public VisitedPlacesRequest createFromParcel(Parcel in) {
            return new VisitedPlacesRequest(in);
        }

        @Override
        public VisitedPlacesRequest[] newArray(int size) {
            return new VisitedPlacesRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(trip_id);
        parcel.writeLong(user_id);
        parcel.writeLong(related_id);
        parcel.writeString(related_type);
        parcel.writeString(field);
    }

}
