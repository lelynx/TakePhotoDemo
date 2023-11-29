package com.example.takephotodemo.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;


public class MediaData implements Parcelable {

    private Uri uri;

    private String description;

    protected MediaData(Parcel in) {
        uri = in.readParcelable(Uri.class.getClassLoader());
        description = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(uri, flags);
        dest.writeString(description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaData> CREATOR = new Creator<MediaData>() {
        @Override
        public MediaData createFromParcel(Parcel in) {
            return new MediaData(in);
        }

        @Override
        public MediaData[] newArray(int size) {
            return new MediaData[size];
        }
    };

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MediaData(Uri uri, String description) {
        this.uri = uri;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaData mediaData = (MediaData) o;
        return Objects.equals(uri, mediaData.uri) && Objects.equals(description, mediaData.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, description);
    }
    @NonNull
    @Override
    public String toString() {
        return "MediaData{" +
                "uri=" + uri +
                ", description='" + description + '\'' +
                '}';
    }
}
