package com.example.advancedpictures;


import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.databind.JsonNode;

public class Picture implements Parcelable {
    private final String id;
    private final String description;
    private final String url;

    public static final Parcelable.Creator<Picture> CREATOR = new Parcelable.Creator<Picture>() {
        public Picture createFromParcel(Parcel in) {return new Picture(in);}
        public Picture[] newArray(int size) {return new Picture[size];}
    };

    Picture(int id, String description, String url) {
        this.id = String.valueOf(id);
        this.description = description;
        this.url = url;
    }

    Picture(int id, JsonNode picture) {
        this.id = String.valueOf(id);
        description = picture.get("title").asText();
        url = getFullSize(picture.get("media").get("m").asText());
    }

    private static String getFullSize(String pureUrl) {
        String res = pureUrl.substring(0, pureUrl.length() - 5);
        return res + "c.jpg";
    }

    String getDescription() {return description;}

    public String getId() {return id;}

    String getUrl() {return url;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(description);
        dest.writeString(url);
    }

    private Picture(Parcel parcel) {
        id = parcel.readString();
        description = parcel.readString();
        url = parcel.readString();
    }
}