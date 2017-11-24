package com.shuai.futures.event;

import com.shuai.futures.data.FuturesInfo;

/**
 *
 */
public class RemoveFollowFuturesEvent {
    private FuturesInfo mItem;

    public RemoveFollowFuturesEvent(FuturesInfo item) {
        this.mItem = item;
    }

    public FuturesInfo getItem() {
        return mItem;
    }

    public void setItem(FuturesInfo item) {
        this.mItem = item;
    }
}
