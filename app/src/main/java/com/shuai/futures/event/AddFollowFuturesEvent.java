package com.shuai.futures.event;

import com.shuai.futures.data.FuturesInfo;

/**
 *
 */
public class AddFollowFuturesEvent {
    private FuturesInfo mItem;

    public AddFollowFuturesEvent(FuturesInfo item) {
        this.mItem = item;
    }

    public FuturesInfo getItem() {
        return mItem;
    }

    public void setItem(FuturesInfo item) {
        this.mItem = item;
    }
}
