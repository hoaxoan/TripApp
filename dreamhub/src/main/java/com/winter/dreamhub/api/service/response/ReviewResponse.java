package com.winter.dreamhub.api.service.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.winter.dreamhub.api.service.model.User;

/**
 * Created by hoaxoan on 11/5/2017.
 */
public class ReviewResponse implements Parcelable {

    public final long id;
    public final String title;
    public final String text;
    public final int rating;
    public final String reviewerName;
    public final String reviewerImageUrl;
    public final long timeStamp;
    public final long entity_id;
    public final long user_id;
    public final User user;
    public final String created_at;
    public final String updated_at;

    public ReviewResponse(long id,
                          String title,
                          String text,
                          int rating,
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

    protected ReviewResponse(Parcel in) {
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

    /* Parcelable stuff */

    @SuppressWarnings("unused")
    public static final Creator<ReviewResponse> CREATOR = new Creator<ReviewResponse>() {
        @Override
        public ReviewResponse createFromParcel(Parcel in) {
            return new ReviewResponse(in);
        }

        @Override
        public ReviewResponse[] newArray(int size) {
            return new ReviewResponse[size];
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
        dest.writeInt(rating);
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
