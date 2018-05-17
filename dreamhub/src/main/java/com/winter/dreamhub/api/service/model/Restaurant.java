package com.winter.dreamhub.api.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.winter.dreamhub.api.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 12/11/2016.
 */

public class Restaurant extends BaseModel implements Parcelable {

    public String name;

    public Restaurant(long id,
                      String title,
                      String name) {
        super(id, title, null);
        this.name = name;
    }

    protected Restaurant(Parcel in) {
        super(in.readLong(), in.readString(), in.readString());
        name = in.readString();
    }

    public static class Builder {
        private long id;
        private String title;
        private String name;

        public Restaurant.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Restaurant.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Restaurant.Builder setName(String name) {
            this.name = name;
            return this;
        }


        public Restaurant build() {
            return new Restaurant(id, title, name);
        }

        public static Restaurant.Builder from(Restaurant existing) {
            return new Restaurant.Builder()
                    .setId(existing.id)
                    .setTitle(existing.title)
                    .setName(existing.name);
        }
    }

    /* Parcelable stuff */

    @SuppressWarnings("unused")
    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
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
        dest.writeString(name);
    }
}
