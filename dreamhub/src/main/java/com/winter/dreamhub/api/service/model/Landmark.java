package com.winter.dreamhub.api.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.winter.dreamhub.api.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 12/11/2016.
 */

public class Landmark extends BaseModel implements Parcelable {

    public String indoorType;

    public Landmark(long id,
                  String title,
                  String indoorType) {
        super(id, title, null);
        this.indoorType = indoorType;
    }

    protected Landmark(Parcel in) {
        super(in.readLong(), in.readString(), in.readString());
        indoorType = in.readString();
    }

    /* Parcelable stuff */

    @SuppressWarnings("unused")
    public static final Creator<Landmark> CREATOR = new Creator<Landmark>() {
        @Override
        public Landmark createFromParcel(Parcel in) {
            return new Landmark(in);
        }

        @Override
        public Landmark[] newArray(int size) {
            return new Landmark[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }
}
