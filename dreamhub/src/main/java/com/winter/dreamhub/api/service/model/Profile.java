package com.winter.dreamhub.api.service.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hoaxoan on 10/14/2017.
 */

public class Profile implements Parcelable {
    public final long id;
    public final String name;
    public final int position;
    public final String thumbnailUrl;

    public Profile(long id,
                   String name,
                   int position,
                   String thumbnailUrl) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.thumbnailUrl = thumbnailUrl;
    }

    protected Profile(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.position = in.readInt();
        this.thumbnailUrl = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
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

        public Profile.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Profile.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Profile.Builder setPosition(int position) {
            this.position = position;
            return this;
        }

        public Profile.Builder setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
            return this;
        }

        public Profile build() {
            return new Profile(id, name, position, thumbnailUrl);
        }

        public static Profile.Builder from(Profile existing) {
            return new Profile.Builder()
                    .setId(existing.id)
                    .setName(existing.name)
                    .setPosition(existing.position)
                    .setThumbnailUrl(existing.thumbnailUrl);
        }
    }

}

