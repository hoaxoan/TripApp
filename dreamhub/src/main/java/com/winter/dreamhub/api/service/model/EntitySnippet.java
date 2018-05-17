package com.winter.dreamhub.api.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.winter.dreamhub.api.model.BaseModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hoaxoan on 12/9/2016.
 */

public class EntitySnippet extends BaseModel implements Parcelable {
    public String name;
    public String description;
    public String photo;
    public String address;
    public String phone;
    public Integer watch;
    public Integer latE7;
    public Integer lngE7;
    public String website;
    public Float ratingHistogram;
    public Integer reviewsCount;
    public Integer voteCount;

    public List<Category> categories;
    public List<Image> photos;
    public List<OpenHoursForDay> openHoursForDays;
    public AggregateRating aggregateRating;
    public User user;

    public EntitySnippet(long id,
                         String title,
                         String name,
                         String description,
                         String photo,
                         String address,
                         String phone,
                         Integer watch,
                         Integer latE7,
                         Integer lngE7,
                         String website,
                         Float ratingHistogram,
                         Integer reviewsCount,
                         Integer voteCount,
                         List<Category> categories,
                         List<Image> photos,
                         List<OpenHoursForDay> openHoursForDays,
                         AggregateRating aggregateRating,
                         User user) {
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
        this.categories = categories;
        this.photos = photos;
        this.openHoursForDays = openHoursForDays;
        this.aggregateRating = aggregateRating;
        this.user = user;
    }

    protected EntitySnippet(Parcel in) {
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
        if (in.readByte() == 0x01) {
            categories = new ArrayList<Category>();
            in.readList(categories, Category.class.getClassLoader());
        } else {
            categories = null;
        }
        if (in.readByte() == 0x01) {
            photos = new ArrayList<Image>();
            in.readList(photos, Image.class.getClassLoader());
        } else {
            photos = null;
        }
        if (in.readByte() == 0x01) {
            openHoursForDays = new ArrayList<OpenHoursForDay>();
            in.readList(openHoursForDays, OpenHoursForDay.class.getClassLoader());
        } else {
            openHoursForDays = null;
        }
        aggregateRating = (AggregateRating) in.readValue(AggregateRating.class.getClassLoader());
        user = (User) in.readValue(User.class.getClassLoader());
    }


    public static class Builder {
        private long id;
        private String title;
        private String name;
        private String description;
        private String photo;
        private String address;
        private String phone;
        private Integer watch;
        private Integer latE7;
        private Integer lngE7;
        private String website;
        private Float ratingHistogram;
        private Integer reviewsCount;
        private Integer voteCount;
        private List<Category> categories;
        private List<Image> photos;
        private List<OpenHoursForDay> openHoursForDays;
        private AggregateRating aggregateRating;
        private User user;

        public EntitySnippet.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public EntitySnippet.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public EntitySnippet.Builder setName(String name) {
            this.name = name;
            return this;
        }

        public EntitySnippet.Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public EntitySnippet.Builder setPhoto(String photo) {
            this.photo = photo;
            return this;
        }

        public EntitySnippet.Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public EntitySnippet.Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public EntitySnippet.Builder setWatch(Integer watch) {
            this.watch = watch;
            return this;
        }

        public EntitySnippet.Builder setLatE7(Integer latE7) {
            this.latE7 = latE7;
            return this;
        }

        public EntitySnippet.Builder setLngE7(Integer lngE7) {
            this.lngE7 = lngE7;
            return this;
        }

        public EntitySnippet.Builder setWebsite(String website) {
            this.website = website;
            return this;
        }

        public EntitySnippet.Builder setRatingHistogram(Float ratingHistogram) {
            this.ratingHistogram = ratingHistogram;
            return this;
        }

        public EntitySnippet.Builder setReviewsCount(Integer reviewsCount) {
            this.reviewsCount = reviewsCount;
            return this;
        }

        public EntitySnippet.Builder setVoteCount(Integer voteCount) {
            this.voteCount = voteCount;
            return this;
        }

        public EntitySnippet.Builder setCategories(List<Category> categories) {
            this.categories = categories;
            return this;
        }

        public EntitySnippet.Builder setPhotos(List<Image> photos) {
            this.photos = photos;
            return this;
        }

        public EntitySnippet.Builder setOpenHoursForDays(List<OpenHoursForDay> openHoursForDays) {
            this.openHoursForDays = openHoursForDays;
            return this;
        }

        public EntitySnippet.Builder setAggregateRating(AggregateRating aggregateRating) {
            this.aggregateRating = aggregateRating;
            return this;
        }

        public EntitySnippet.Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public EntitySnippet build() {
            return new EntitySnippet(id, title, name, description, photo, address, phone,
                    watch, latE7, lngE7, website, ratingHistogram,
                    reviewsCount, voteCount,
                    categories, photos, openHoursForDays,
                    aggregateRating, user);
        }

        public static EntitySnippet.Builder from(EntitySnippet existing) {
            return new EntitySnippet.Builder()
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
                    .setPhotos(existing.photos)
                    .setOpenHoursForDays(existing.openHoursForDays)
                    .setAggregateRating(existing.aggregateRating)
                    .setUser(existing.user);
        }
    }

    /* Parcelable stuff */

    @SuppressWarnings("unused")
    public static final Creator<EntitySnippet> CREATOR = new Creator<EntitySnippet>() {
        @Override
        public EntitySnippet createFromParcel(Parcel in) {
            return new EntitySnippet(in);
        }

        @Override
        public EntitySnippet[] newArray(int size) {
            return new EntitySnippet[size];
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
        if (categories == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(categories);
        }
        if (photos == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(photos);
        }
        if (openHoursForDays == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(openHoursForDays);
        }
        dest.writeValue(aggregateRating);
        dest.writeValue(user);
    }
}

