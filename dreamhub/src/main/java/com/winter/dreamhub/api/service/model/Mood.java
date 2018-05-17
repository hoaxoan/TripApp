package com.winter.dreamhub.api.service.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by hoaxoan on 12/6/2016.
 */

public class Mood implements Parcelable {
    public final long id;
    public final String name;
    public final long position;
    public final String thumbnailUrl;

    public Mood(long id,
                String name,
                long position,
                String thumbnailUrl) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.thumbnailUrl = thumbnailUrl;
    }

    protected Mood(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.position = in.readLong();
        this.thumbnailUrl = in.readString();
    }

    public static final Creator<Mood> CREATOR = new Creator<Mood>() {
        @Override
        public Mood createFromParcel(Parcel in) {
            return new Mood(in);
        }

        @Override
        public Mood[] newArray(int size) {
            return new Mood[size];
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
        private long position;
        private String thumbnailUrl;

        public Mood.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Mood.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Mood.Builder setPosition(long position) {
            this.position = position;
            return this;
        }

        public Mood.Builder setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
            return this;
        }

        public Mood build() {
            return new Mood(id, name, position, thumbnailUrl);
        }

        public static Mood.Builder from(Mood existing) {
            return new Mood.Builder()
                    .setId(existing.id)
                    .setName(existing.name)
                    .setPosition(existing.position)
                    .setThumbnailUrl(existing.thumbnailUrl);
        }
    }

}
