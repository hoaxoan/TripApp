package com.winter.dreamhub.api.service.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hoaxoan on 10/14/2017.
 */

public class Amenity implements Parcelable {
    public final long id;
    public final String name;
    public final int position;
    public final String thumbnailUrl;

    public Amenity(long id,
                String name,
                   int position,
                String thumbnailUrl) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.thumbnailUrl = thumbnailUrl;
    }

    protected Amenity(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.position = in.readInt();
        this.thumbnailUrl = in.readString();
    }

    public static final Creator<Amenity> CREATOR = new Creator<Amenity>() {
        @Override
        public Amenity createFromParcel(Parcel in) {
            return new Amenity(in);
        }

        @Override
        public Amenity[] newArray(int size) {
            return new Amenity[size];
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
        parcel.writeLong(position);
        parcel.writeString(thumbnailUrl);
    }

    public static class Builder {
        private long id;
        private String name;
        private int position;
        private String thumbnailUrl;

        public Amenity.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Amenity.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Amenity.Builder setPosition(int position) {
            this.position = position;
            return this;
        }

        public Amenity.Builder setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
            return this;
        }

        public Amenity build() {
            return new Amenity(id, name, position, thumbnailUrl);
        }

        public static Amenity.Builder from(Amenity existing) {
            return new Amenity.Builder()
                    .setId(existing.id)
                    .setName(existing.name)
                    .setPosition(existing.position)
                    .setThumbnailUrl(existing.thumbnailUrl);
        }
    }

}

