package com.winter.dreamhub.api.service.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hoaxoan on 11/5/2017.
 */

public class AggregateRating implements Parcelable {
    public final long id;
    public final long reviewsCount;
    public final long ratingsCount;
    public final float starRating;
    public final long oneStarRatings;
    public final long twoStarRatings;
    public final long threeStarRatings;
    public final long fourStarRatings;
    public final long fiveStarRatings;

    public AggregateRating(long id,
                   long reviewsCount,
                   long ratingsCount,
                   float starRating,
                   long oneStarRatings,
                   long twoStarRatings,
                   long threeStarRatings,
                   long fourStarRatings,
                   long fiveStarRatings) {
        this.id = id;
        this.reviewsCount = reviewsCount;
        this.ratingsCount = ratingsCount;
        this.starRating = starRating;
        this.oneStarRatings = oneStarRatings;
        this.twoStarRatings = twoStarRatings;
        this.threeStarRatings = threeStarRatings;
        this.fourStarRatings = fourStarRatings;
        this.fiveStarRatings = fiveStarRatings;
    }

    protected AggregateRating(Parcel in) {
        this.id = in.readLong();
        this.reviewsCount = in.readLong();
        this.ratingsCount = in.readLong();
        this.starRating = in.readFloat();
        this.oneStarRatings = in.readLong();
        this.twoStarRatings = in.readLong();
        this.threeStarRatings = in.readLong();
        this.fourStarRatings = in.readLong();
        this.fiveStarRatings = in.readLong();
    }

    public static final Creator<AggregateRating> CREATOR = new Creator<AggregateRating>() {
        @Override
        public AggregateRating createFromParcel(Parcel in) {
            return new AggregateRating(in);
        }

        @Override
        public AggregateRating[] newArray(int size) {
            return new AggregateRating[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(reviewsCount);
        parcel.writeLong(ratingsCount);
        parcel.writeFloat(starRating);
        parcel.writeLong(oneStarRatings);
        parcel.writeLong(twoStarRatings);
        parcel.writeLong(threeStarRatings);
        parcel.writeLong(fourStarRatings);
        parcel.writeLong(fiveStarRatings);
    }

    public static class Builder {
        private long id;
        private long reviewsCount;
        private long ratingsCount;
        private float starRating;
        private long oneStarRatings;
        private long twoStarRatings;
        private long threeStarRatings;
        private long fourStarRatings;
        private long fiveStarRatings;

        public AggregateRating.Builder setId(long id) {
            this.id = id;
            return this;
        }

        public AggregateRating.Builder setReviewsCount(long reviewsCount) {
            this.reviewsCount = reviewsCount;
            return this;
        }

        public AggregateRating.Builder setRatingsCount(long ratingsCount) {
            this.ratingsCount = ratingsCount;
            return this;
        }

        public AggregateRating.Builder setStarRating(float starRating) {
            this.starRating = starRating;
            return this;
        }

        public AggregateRating.Builder setOneStarRatings(long oneStarRatings) {
            this.oneStarRatings = oneStarRatings;
            return this;
        }

        public AggregateRating.Builder setTwoStarRatings(long twoStarRatings) {
            this.twoStarRatings = twoStarRatings;
            return this;
        }

        public AggregateRating.Builder setThreeStarRatings(long threeStarRatings) {
            this.threeStarRatings = threeStarRatings;
            return this;
        }

        public AggregateRating.Builder setFourStarRatings(long fourStarRatings) {
            this.fourStarRatings = fourStarRatings;
            return this;
        }

        public AggregateRating.Builder setFiveStarRatings(long fiveStarRatings) {
            this.fiveStarRatings = fiveStarRatings;
            return this;
        }

        public AggregateRating build() {
            return new AggregateRating(id, reviewsCount, ratingsCount, starRating, oneStarRatings,
                    twoStarRatings, threeStarRatings, fourStarRatings, fiveStarRatings);
        }

        public static AggregateRating.Builder from(AggregateRating existing) {
            return new AggregateRating.Builder()
                    .setId(existing.id)
                    .setReviewsCount(existing.reviewsCount)
                    .setRatingsCount(existing.ratingsCount)
                    .setStarRating(existing.starRating)
                    .setOneStarRatings(existing.oneStarRatings)
                    .setTwoStarRatings(existing.twoStarRatings)
                    .setThreeStarRatings(existing.threeStarRatings)
                    .setFourStarRatings(existing.fourStarRatings)
                    .setFiveStarRatings(existing.fiveStarRatings);
        }
    }

}

