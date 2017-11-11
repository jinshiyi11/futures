package com.shuai.futures.event;

import com.shuai.futures.data.FuturesInfo;

/**
 *
 */
public class FollowedFuturesAddedEvent {
    private FuturesInfo mItem;

    public FollowedFuturesAddedEvent(FuturesInfo item) {
        this.mItem = item;
    }

    public FuturesInfo getItem() {
        return mItem;
    }

    public void setItem(FuturesInfo item) {
        this.mItem = item;
    }
}
