package com.winter.dreamhub.api.service.model;

import android.content.res.ColorStateList;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.text.Spanned;
import android.text.TextUtils;

import com.winter.dreamhub.api.model.BaseModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hoaxoan on 12/4/2016.
 */

public class Entity extends BaseModel implements Parcelable {

    public String name;
    public String description;
    public String photo;
    public String address;
    public String phone;
    public int watch;
    public int latE7;
    public int lngE7;
    public String website;
    public float ratingHistogram;
    public int reviewsCount;
    public int voteCount;
    public int type;
    public long trip_id;

    public List<Category> categories;
    public List<Image> photos;
    public List<OpenHoursForDay> openHoursForDays;
    public List<Amenity> amenities;
    public AggregateRating aggregateRating;

    public Landmark landmark;
    public Hotel hotel;
    public Restaurant restaurant;
    public VisitedPlace visitedPlace;

    public Entity(long id,
                    String title,
                    String name,
                    String description,
                    String photo,
                    String address,
                    String phone,
                    int watch,
                    int latE7,
                    int lngE7,
                    String website,
                    float ratingHistogram,
                    int reviewsCount,
                    int voteCount,
                    int type,
                    long trip_id,
                    List<Category> categories,
                    List<Image> photos,
                    List<OpenHoursForDay> openHoursForDays,
                    List<Amenity> amenities,
                    AggregateRating aggregateRating,
                    Landmark landmark,
                    Hotel hotel,
                    Restaurant restaurant,
                    VisitedPlace visitedPlace) {
        super(id, title, null);
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.address = address;
        this.phone = phone;
        this.watch = watch;
        this.latE7 = latE7;
        this.lngE7 = lngE7;
        this.website = website;
        this.ratingHistogram = ratingHistogram;
        this.reviewsCount = reviewsCount;
        this.voteCount = voteCount;
        this.type = type;
        this.trip_id = trip_id;
        this.categories = categories;
        this.photos = photos;
        this.openHoursForDays = openHoursForDays;
        this.amenities = amenities;
        this.aggregateRating = aggregateRating;
        this.landmark = landmark;
        this.hotel = hotel;
        this.restaurant = restaurant;
        this.visitedPlace = visitedPlace;
    }

    protected Entity(Parcel in) {
        super(in.readLong(), in.readString(), in.readString());
        name = in.readString();
        description = in.readString();
        photo = in.readString();
        address = in.readString();
        phone = in.readString();
        watch = in.readInt();
        latE7 = in.readInt();
        lngE7 = in.readInt();
        website = in.readString();
        ratingHistogram = in.readFloat();
        reviewsCount = in.readInt();
        voteCount = in.readInt();
        type = in.readInt();
        trip_id = in.readLong();
        if (in.readByte() == 0x01) {
            categories = new ArrayList<Category>();
            in.readTypedList(categories, Category.CREATOR);
        } else {
            categories = null;
        }
        if (in.readByte() == 0x01) {
            photos = new ArrayList<Image>();
            in.readTypedList(photos, Image.CREATOR);
        } else {
            photos = null;
        }
        if (in.readByte() == 0x01) {
            openHoursForDays = new ArrayList<OpenHoursForDay>();
            in.readTypedList(openHoursForDays, OpenHoursForDay.CREATOR);
        } else {
            openHoursForDays = null;
        }
        if (in.readByte() == 0x01) {
            amenities = new ArrayList<Amenity>();
            in.readTypedList(amenities, Amenity.CREATOR);
        } else {
            amenities = null;
        }
        aggregateRating = (AggregateRating) in.readParcelable(AggregateRating.class.getClassLoader());
        landmark = (Landmark) in.readParcelable(Landmark.class.getClassLoader());
        hotel = (Hotel) in.readParcelable(Hotel.class.getClassLoader());
        restaurant = (Restaurant) in.readParcelable(Restaurant.class.getClassLoader());
        visitedPlace = (VisitedPlace) in.readParcelable(VisitedPlace.class.getClassLoader());
    }

    public static class Builder {
        private long id;
        private String title;
        private String name;
        private String description;
        private String photo;
        private String address;
        private String phone;
        private int watch;
        private int latE7;
        private int lngE7;
        private String website;
        private float ratingHistogram;
        private int reviewsCount;
        private int voteCount;
        private int type;
        private long trip_id;
        private List<Category> categories;
        private List<Image> photos;
        private List<OpenHoursForDay> openHoursForDays;
        private List<Amenity> amenities;
        private AggregateRating aggregateRating;
        private Landmark landmark;
        private Hotel hotel;
        private Restaurant restaurant;
        private VisitedPlace visitedPlace;

        public Entity.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Entity.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Entity.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Entity.Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Entity.Builder setPhoto(String photo) {
            this.photo = photo;
            return this;
        }

        public Entity.Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Entity.Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Entity.Builder setWatch(int watch) {
            this.watch = watch;
            return this;
        }

        public Entity.Builder setLatE7(int latE7) {
            this.latE7 = latE7;
            return this;
        }

        public Entity.Builder setLngE7(int lngE7) {
            this.lngE7 = lngE7;
            return this;
        }

        public Entity.Builder setWebsite(String website) {
            this.website = website;
            return this;
        }

        public Entity.Builder setRatingHistogram(float ratingHistogram) {
            this.ratingHistogram = ratingHistogram;
            return this;
        }

        public Entity.Builder setReviewsCount(int reviewsCount) {
            this.reviewsCount = reviewsCount;
            return this;
        }

        public Entity.Builder setVoteCount(int voteCount) {
            this.voteCount = voteCount;
            return this;
        }

        public Entity.Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Entity.Builder setTripId(long trip_id) {
            this.trip_id = trip_id;
            return this;
        }

        public Entity.Builder setCategories(List<Category> categories) {
            this.categories = categories;
            return this;
        }

        public Entity.Builder setPhotos(List<Image> photos) {
            this.photos = photos;
            return this;
        }

        public Entity.Builder setOpenHoursForDays(List<OpenHoursForDay> openHoursForDays) {
            this.openHoursForDays = openHoursForDays;
            return this;
        }

        public Entity.Builder setAmenities(List<Amenity> amenities) {
            this.amenities = amenities;
            return this;
        }

        public Entity.Builder setAggregateRating(AggregateRating aggregateRating) {
            this.aggregateRating = aggregateRating;
            return this;
        }

        public Entity.Builder setLandmark(Landmark landmark) {
            this.landmark = landmark;
            return this;
        }

        public Entity.Builder setHotel(Hotel hotel) {
            this.hotel = hotel;
            return this;
        }

        public Entity.Builder setRestaurant(Restaurant restaurant) {
            this.restaurant = restaurant;
            return this;
        }

        public Entity.Builder setVisitedPlace(VisitedPlace visitedPlace){
            this.visitedPlace = visitedPlace;
            return this;
        }
        public Entity build() {
            return new Entity(id, title, name, description, photo, address, phone,
                    watch, latE7, lngE7, website, ratingHistogram,
                    reviewsCount, voteCount, type, trip_id,
                    categories, photos, openHoursForDays, amenities,
                    aggregateRating, landmark, hotel, restaurant, visitedPlace);
        }

        public static Entity.Builder from(Entity existing) {
            return new Entity.Builder()
                    .setId(existing.id)
                    .setTitle(existing.title)
                    .setName(existing.name)
                    .setDescription(existing.description)
                    .setPhoto(existing.photo)
                    .setAddress(existing.address)
                    .setPhone(existing.phone)
                    .setWatch(existing.watch)
                    .setLatE7(existing.latE7)
                    .setLngE7(existing.lngE7)
                    .setWebsite(existing.website)
                    .setRatingHistogram(existing.ratingHistogram)
                    .setReviewsCount(existing.reviewsCount)
                    .setVoteCount(existing.voteCount)
                    .setType(existing.type)
                    .setTripId(existing.trip_id)
                    .setCategories(existing.categories)
                    .setPhotos(existing.photos)
                    .setOpenHoursForDays(existing.openHoursForDays)
                    .setAmenities(existing.amenities)
                    .setAggregateRating(existing.aggregateRating)
                    .setLandmark(existing.landmark)
                    .setHotel(existing.hotel)
                    .setRestaurant(existing.restaurant)
                    .setVisitedPlace(existing.visitedPlace);
        }
    }

    /* Parcelable stuff */

    @SuppressWarnings("unused")
    public static final Creator<Entity> CREATOR = new Creator<Entity>() {
        @Override
        public Entity createFromParcel(Parcel in) {
            return new Entity(in);
        }

        @Override
        public Entity[] newArray(int size) {
            return new Entity[size];
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
        dest.writeString(description);
        dest.writeString(photo);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeInt(watch);
        dest.writeInt(latE7);
        dest.writeInt(latE7);
        dest.writeString(website);
        dest.writeFloat(ratingHistogram);
        dest.writeInt(reviewsCount);
        dest.writeInt(voteCount);
        dest.writeInt(type);
        dest.writeLong(trip_id);
        if (categories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeTypedList(categories);
        }
        if (photos == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeTypedList(photos);
        }
        if (openHoursForDays == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeTypedList(openHoursForDays);
        }
        if (amenities == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeTypedList(amenities);
        }
        dest.writeParcelable(aggregateRating, flags);
        dest.writeParcelable(landmark, flags);
        dest.writeParcelable(hotel, flags);
        dest.writeParcelable(restaurant, flags);
        dest.writeParcelable(visitedPlace, flags);
    }
}
