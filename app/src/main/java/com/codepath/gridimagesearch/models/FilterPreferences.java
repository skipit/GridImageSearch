package com.codepath.gridimagesearch.models;

import android.os.Parcel;
import android.os.Parcelable;


public class FilterPreferences implements Parcelable {
    public String size;
    public String color;
    public String type;
    public String site;

    public FilterPreferences() {
        this.size = null;
        this.color = null;
        this.type = null;
        this.site = null;
    }

    public FilterPreferences(String size, String color, String type, String site) {
        this.size = size;
        this.color = color;
        this.type = type;
        this.site = site.trim();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.size);
        dest.writeString(this.color);
        dest.writeString(this.type);
        dest.writeString(this.site);
    }

    private FilterPreferences(Parcel in) {
        this.size = in.readString();
        this.color = in.readString();
        this.type = in.readString();
        this.site = in.readString();
    }

    @Override
    public String toString() {
        return "[" + this.size + "," + this.color + "," + this.type + "," + this.site +"]";
    }

    public static Parcelable.Creator<FilterPreferences> CREATOR = new Parcelable.Creator<FilterPreferences>() {
        public FilterPreferences createFromParcel(Parcel source) {
            return new FilterPreferences(source);
        }

        public FilterPreferences[] newArray(int size) {
            return new FilterPreferences[size];
        }
    };
}
