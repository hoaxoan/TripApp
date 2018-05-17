package com.winter.dreamhub.api.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.winter.dreamhub.api.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 12/9/2016.
 */

public class Trip extends BaseModel implements Parcelable {

    public final String name;
    public final String photo;
    public final List<Category> categories;
    public final List<Mood> moods;
    public final List<Image> photos;

    public Trip(long id,
                String title,
                String name,
                String photo,
                List<Category> categories,
                List<Mood> moods,
                List<Image> photos) {
        super(id, title, null);
        this.name = name;
        this.photo = photo;
        this.categories = categories;
        this.moods = moods;
        this.photos = photos;
    }

    protected Trip(Parcel in) {
        super(in.readLong(), in.readString(), in.readString());
        name = in.readString();
        photo = in.readString();
        if (in.readByte() == 0x01) {
            categories = new ArrayList<Category>();
            in.readList(categories, Category.class.getClassLoader());
        } else {
            categories = null;
        }
        if (in.readByte() == 0x01) {
            moods = new ArrayList<Mood>();
            in.readList(moods, Mood.class.getClassLoader());
        } else {
            moods = null;
        }
        if (in.readByte() == 0x01) {
            photos = new ArrayList<Image>();
            in.readList(photos, Image.class.getClassLoader());
        } else {
            photos = null;
        }
    }

    public static class Builder {
        private long id;
        private String title;
        private String name;
        private String photo;
        private List<Category> categories;
        private List<Mood> moods;
        private List<Image> photos;

        public Trip.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Trip.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Trip.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Trip.Builder setPhoto(String photo) {
            this.photo = photo;
            return this;
        }

        public Trip.Builder setCategories(List<Category> categories) {
            this.categories = categories;
            return this;
        }

        public Trip.Builder setMoods(List<Mood> moods) {
            this.moods = moods;
            return this;
        }

        public Trip.Builder setPhotos(List<Image> photos) {
            this.photos = photos;
            return this;
        }

        public Trip build() {
            return new Trip(id, title, name, photo, categories, moods, photos);
        }

        public static Trip.Builder from(Trip existing) {
            return new Trip.Builder()
                    .setId(existing.id)
                    .setTitle(existing.title)
                    .setName(existing.name)
                    .setPhoto(existing.photo)
                    .setCategories(existing.categories)
                    .setMoods(existing.moods)
                    .setPhotos(existing.photos);
        }
    }

    /* Parcelable stuff */

    @SuppressWarnings("unused")
    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
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
        dest.writeString(photo);
        if (categories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(categories);
        }
        if (moods == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(moods);
        }
        if (photos == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(photos);
        }
    }
}
