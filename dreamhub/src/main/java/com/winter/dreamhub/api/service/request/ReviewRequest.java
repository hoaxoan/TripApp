package com.winter.dreamhub.api.service.request;

import android.media.Rating;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.winter.dreamhub.api.service.model.Review;
import com.winter.dreamhub.api.service.model.User;

/**
 * Created by hoaxoan on 11/5/2017.
 */
public class ReviewRequest implements Parcelable {

    public final String title;
    public final String text;
    public final float rating;
    public final String reviewerName;
    public String reviewerImageUrl;
    public long timeStamp;
    public final long entity_id;
    public long user_id;

    public ReviewRequest(String title,
                         String text,
                         float rating,
                         String reviewerName,
                         String reviewerImageUrl,
                         long timeStamp,
                         long entity_id,
                         long user_id) {
        this.title = title;
        this.text = text;
        this.rating = rating;
        this.reviewerName = reviewerName;
        this.reviewerImageUrl = reviewerImageUrl;
        this.timeStamp = timeStamp;
        this.entity_id = entity_id;
        this.user_id = user_id;
    }

    public ReviewRequest(String title,
                         String text,
                         float rating,
                         String reviewerName,
                         long entity_id,
                         long user_id) {
        this.title = title;
        this.text = text;
        this.rating = rating;
        this.reviewerName = reviewerName;
        this.entity_id = entity_id;
        this.user_id = user_id;
    }

    protected ReviewRequest(Parcel in) {
        title = in.readString();
        text = in.readString();
        rating = in.readInt();
        reviewerName = in.readString();
        reviewerImageUrl = in.readString();
        timeStamp = in.readLong();
        entity_id = in.readLong();
        user_id = in.readLong();
    }

    /* Parcelable stuff */

    @SuppressWarnings("unused")
    public static final Creator<ReviewRequest> CREATOR = new Creator<ReviewRequest>() {
        @Override
        public ReviewRequest createFromParcel(Parcel in) {
            return new ReviewRequest(in);
        }

        @Override
        public ReviewRequest[] newArray(int size) {
            return new ReviewRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(text);
        dest.writeFloat(rating);
        dest.writeString(reviewerName);
        dest.writeString(reviewerImageUrl);
        dest.writeLong(timeStamp);
        dest.writeLong(entity_id);
        dest.writeLong(user_id);
    }

}
