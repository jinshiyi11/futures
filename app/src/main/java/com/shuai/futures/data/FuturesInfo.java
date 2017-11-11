package com.shuai.futures.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class FuturesInfo implements Parcelable {
    public FuturesInfo() {

    }

    public FuturesInfo(String id, String name, String title) {
        this.mId = id;
        this.mName = name;
        this.mTitle = title;
    }

    @SerializedName("id")
    public String mId;

    @SerializedName("name")
    public String mName;

    @SerializedName("title")
    public String mTitle;

    protected FuturesInfo(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mTitle = in.readString();
    }

    public static final Creator<FuturesInfo> CREATOR = new Creator<FuturesInfo>() {
        @Override
        public FuturesInfo createFromParcel(Parcel in) {
            return new FuturesInfo(in);
        }

        @Override
        public FuturesInfo[] newArray(int size) {
            return new FuturesInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mTitle);
    }
}
