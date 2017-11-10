package com.shuai.futures.data;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class FuturesInfo {
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
}
