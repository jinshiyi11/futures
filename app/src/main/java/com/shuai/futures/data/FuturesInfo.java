package com.shuai.futures.data;

import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class FuturesInfo {
    public FuturesInfo(){

    }

    public FuturesInfo(String id/*, String name*/) {
        this.mId = id;
//        this.mName = name;
    }

    @SerializedName("id")
    public String mId;

//    @SerializedName("name")
//    public String mName;
}
