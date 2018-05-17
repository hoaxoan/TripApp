package com.winter.dreamhub.api.service.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by hoaxoan on 12/3/2016.
 */

public class User implements Parcelable {
    public final long id;
    public final String username;
    public final String email;
    public final String provider;
    public final String password;
    public final String resetPasswordToken;

    public User(long id,
                       String username,
                       String email,
                       String provider,
                       String password,
                       String resetPasswordToken) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.password = password;
        this.resetPasswordToken = resetPasswordToken;
    }

    public static class Builder {
        private long id;
        private String username;
        private String email;
        private String provider;
        private String password;
        private String resetPasswordToken;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setProvider(String provider) {
            this.provider = provider;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setResetPasswordToken(String resetPasswordToken) {
            this.resetPasswordToken = resetPasswordToken;
            return this;
        }

        public User build() {
            return new User(id, username, email, provider, password, resetPasswordToken);
        }
    }

    protected User(Parcel in) {
        id = in.readLong();
        username = in.readString();
        email = in.readString();
        provider = in.readString();
        password = in.readString();
        resetPasswordToken = in.readString();
    }

     /* Parcelable stuff */

    @SuppressWarnings("unused")
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(provider);
        dest.writeString(password);
        dest.writeString(resetPasswordToken);
    }

    public static String toJSON(User user){
        if(user == null){
            return "";
        }

        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", user.id);
            jsonObj.put("username", user.username);
            jsonObj.put("email", user.email);
            jsonObj.put("provider", user.provider);
            jsonObj.put("password", user.password);
            jsonObj.put("resetPasswordToken", user.resetPasswordToken);
            return jsonObj.toString();
        } catch (JSONException e) {
            return "";
        }
    }

    public static User toUser(String strObj){
        if(TextUtils.isEmpty(strObj)){
            return null;
        }

        try {
            JSONObject jsonObj = new JSONObject(strObj);
            User user = new User(getLongValue("id", jsonObj),
                    getStringValue("username", jsonObj),
                    getStringValue("email", jsonObj),
                    getStringValue("provider", jsonObj),
                    getStringValue("password", jsonObj),
                    getStringValue("resetPasswordToken", jsonObj));
            return user;
        } catch (JSONException e) {
            return null;
        }
    }

    private static long getLongValue(String name, JSONObject jsonObj){
        try{
            return jsonObj.getLong(name);
        }catch (JSONException ex){
            return 0;
        }
    }

    private static String getStringValue(String name, JSONObject jsonObj){
        try{
            return jsonObj.getString(name);
        }catch (JSONException ex){
            return "";
        }
    }
}