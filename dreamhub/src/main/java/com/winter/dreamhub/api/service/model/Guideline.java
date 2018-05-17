package com.winter.dreamhub.api.service.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hoaxoan on 12/6/2016.
 */

public class Guideline implements Parcelable {
    public final long id;
    public final String name;
    public final String description;
    public final long categoryId;
    public final String categoryName;

    public Guideline(long id,
                     String name,
                     String description,
                     long categoryId,
                     String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    protected Guideline(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.description = in.readString();
        this.categoryId = in.readLong();
        this.categoryName = in.readString();
    }

    public static final Creator<Guideline> CREATOR = new Creator<Guideline>() {
        @Override
        public Guideline createFromParcel(Parcel in) {
            return new Guideline(in);
        }

        @Override
        public Guideline[] newArray(int size) {
            return new Guideline[size];
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
        parcel.writeString(description);
        parcel.writeLong(categoryId);
        parcel.writeString(categoryName);
    }

    public static class Builder {
        private long id;
        private String name;
        private String description;
        private long categoryId;
        private String categoryName;

        public Guideline.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Guideline.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Guideline.Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Guideline.Builder setCategoryId(long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Guideline.Builder setCategoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public Guideline build() {
            return new Guideline(id, name, description, categoryId, categoryName);
        }

        public static Guideline.Builder from(Guideline existing) {
            return new Guideline.Builder()
                    .setId(existing.id)
                    .setName(existing.name)
                    .setDescription(existing.description)
                    .setCategoryId(existing.categoryId)
                    .setCategoryName(existing.categoryName);
        }
    }

}
