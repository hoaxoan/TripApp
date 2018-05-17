package com.winter.dreamhub.api.service.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {
    public final int id;
    public final String name;
    public final String size;
    public final String ext;
    public final String mime;
    public final String url;

    public Image(int id,
                 String name,
                 String size,
                 String ext,
                 String mime,
                 String url) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.ext = ext;
        this.mime = mime;
        this.url = url;
    }

    protected Image(Parcel in) {
        id = in.readInt();
        name = in.readString();
        size = in.readString();
        ext = in.readString();
        mime = in.readString();
        url = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(size);
        dest.writeString(ext);
        dest.writeString(mime);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

}