package com.winter.dreamhub.api.service.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Auth implements Parcelable {
    public final String jwt;
    public final User user;

    public Auth(String jwt,
                User user) {
        this.jwt = jwt;
        this.user = user;
    }

    public static class Builder {
        private String jwt;
        private User user;

        public Auth.Builder setJWT(String jwt) {
            this.jwt = jwt;
            return this;
        }

        public Auth.Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Auth build() {
            return new Auth(jwt, user);
        }
    }

    protected Auth(Parcel in) {
        jwt = in.readString();
        user = (User) in.readValue(User.class.getClassLoader());
    }

    /* Parcelable stuff */

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Auth> CREATOR = new Parcelable.Creator<Auth>() {
        @Override
        public Auth createFromParcel(Parcel in) {
            return new Auth(in);
        }

        @Override
        public Auth[] newArray(int size) {
            return new Auth[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jwt);
        dest.writeValue(user);
    }
}
