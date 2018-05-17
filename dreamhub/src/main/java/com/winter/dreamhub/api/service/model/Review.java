package com.winter.dreamhub.api.service.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hoaxoan on 12/9/2016.
 */

public class Review implements Parcelable {

    public final long id;
    public final String title;
    public final String text;
    public final float rating;
    public final String reviewerName;
    public final String reviewerImageUrl;
    public final long timeStamp;
    public final long entity_id;
    public final long user_id;
    public final User user;
    public final String created_at;
    public final String updated_at;

    public Review(long id,
                  String title,
                  String text,
                  float rating,
                  String reviewerName,
                  String reviewerImageUrl,
                  long timeStamp,
                  long entity_id,
                  long user_id,
                  String created_at,
                  String updated_at,
                  User user) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.rating = rating;
        this.reviewerName = reviewerName;
        this.reviewerImageUrl = reviewerImageUrl;
        this.timeStamp = timeStamp;
        this.entity_id = entity_id;
        this.user_id = user_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.user = user;
    }

    protected Review(Parcel in) {
        id = in.readLong();
        title = in.readString();
        text = in.readString();
        rating = in.readInt();
        reviewerName = in.readString();
        reviewerImageUrl = in.readString();
        timeStamp = in.readLong();
        entity_id = in.readLong();
        user_id = in.readLong();
        created_at = in.readString();
        updated_at = in.readString();
        user = (User) in.readValue(User.class.getClassLoader());
    }

    public static class Builder {
        private long id;
        private String title;
        private String text;
        private float rating;
        private String reviewerName;
        private String reviewerImageUrl;
        private long timeStamp;
        private long entity_id;
        private long user_id;
        private User user;
        private String created_at;
        private String updated_at;

        public Review.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Review.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Review.Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Review.Builder setRating(float rating) {
            this.rating = rating;
            return this;
        }

        public Review.Builder setReviewerName(String reviewerName) {
            this.reviewerName = reviewerName;
            return this;
        }

        public Review.Builder setReviewerImageUrl(String reviewerImageUrl) {
            this.reviewerImageUrl = reviewerImageUrl;
            return this;
        }

        public Review.Builder setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Review.Builder setEntityId(long entity_id) {
            this.entity_id = entity_id;
            return this;
        }

        public Review.Builder setUserId(long user_id) {
            this.user_id = user_id;
            return this;
        }

        public Builder setCreatedAt(String created_at) {
            this.created_at = created_at;
            return this;
        }

        public Builder setUpdatedAt(String updated_at) {
            this.updated_at = updated_at;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Review build() {
            return new Review(id, title, text, rating, reviewerName, reviewerImageUrl, timeStamp, entity_id, user_id, created_at, updated_at, user);
        }

        public static Review.Builder from(Review existing) {
            return new Review.Builder()
                    .setId(existing.id)
                    .setTitle(existing.title)
                    .setText(existing.text)
                    .setRating(existing.rating)
                    .setReviewerName(existing.reviewerName)
                    .setReviewerImageUrl(existing.reviewerImageUrl)
                    .setTimeStamp(existing.timeStamp)
                    .setEntityId(existing.entity_id)
                    .setUserId(existing.user_id)
                    .setCreatedAt(existing.created_at)
                    .setUpdatedAt(existing.updated_at)
                    .setUser(existing.user);
        }
    }

    /* Parcelable stuff */

    @SuppressWarnings("unused")
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
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
        dest.writeString(text);
        dest.writeFloat(rating);
        dest.writeString(reviewerName);
        dest.writeString(reviewerImageUrl);
        dest.writeLong(timeStamp);
        dest.writeLong(entity_id);
        dest.writeLong(user_id);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeValue(user);
    }
}
