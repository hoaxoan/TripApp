package com.winter.dreamhub.api.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoaxoan on 12/6/2016.
 */

public class Category implements Parcelable {
    public final long id;
    public final String name;
    public final List<Restaurant> restaurants;

    public Category(long id,
                    String name,
                    List<Restaurant> restaurants) {
        this.id = id;
        this.name = name;
        this.restaurants = restaurants;
    }

    protected Category(Parcel in) {
        id = in.readLong();
        name = in.readString();
        if (in.readByte() == 0x01) {
            restaurants = new ArrayList<Restaurant>();
            in.readList(restaurants, Restaurant.class.getClassLoader());
        } else {
            restaurants = null;
        }
    }

    public static class Builder {
        private long id;
        private String name;
        private List<Restaurant> restaurants;

        public Category.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Category.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Category.Builder setRestaurants(List<Restaurant> restaurants) {
            this.restaurants = restaurants;
            return this;
        }

        public Category build() {
            return new Category(id, name, restaurants);
        }

        public static Builder from(Category existing) {
            return new Builder()
                    .setId(existing.id)
                    .setName(existing.name)
                    .setRestaurants(existing.restaurants);
        }
    }



    /* Parcelable stuff */

    @SuppressWarnings("unused")
    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        if (restaurants == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(restaurants);
        }
    }
}
