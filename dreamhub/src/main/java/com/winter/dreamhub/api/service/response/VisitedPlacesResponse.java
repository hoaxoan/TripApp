package com.winter.dreamhub.api.service.response;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hoaxoan on 12/6/2016.
 */

public class VisitedPlacesResponse implements Parcelable {
    public final long id;
    public final long trip_id;
    public final long user_id;
    public final long related_id;
    public final String related_type;
    public final String field;

    public VisitedPlacesResponse(long id,
                                 long trip_id,
                                 long user_id,
                                 long related_id,
                                 String related_type,
                                 String field) {
        this.id = id;
        this.trip_id = trip_id;
        this.user_id = user_id;
        this.related_id = related_id;
        this.related_type = related_type;
        this.field = field;
    }

    protected VisitedPlacesResponse(Parcel in) {
        this.id = in.readLong();
        this.trip_id = in.readLong();
        this.user_id = in.readLong();
        this.related_id = in.readLong();
        this.related_type = in.readString();
        this.field = in.readString();
    }

    public static final Creator<VisitedPlacesResponse> CREATOR = new Creator<VisitedPlacesResponse>() {
        @Override
        public VisitedPlacesResponse createFromParcel(Parcel in) {
            return new VisitedPlacesResponse(in);
        }

        @Override
        public VisitedPlacesResponse[] newArray(int size) {
            return new VisitedPlacesResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(trip_id);
        parcel.writeLong(user_id);
        parcel.writeLong(related_id);
        parcel.writeString(related_type);
        parcel.writeString(field);
    }

}
