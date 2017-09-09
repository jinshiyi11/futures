package com.shuai.futures.data;

public enum LoadingStatus {
	/**
     * 正在加载并且还没有任何数据
     */
    STATUS_LOADING,
    /**
     * 有数据
     */
    STATUS_GOT_DATA,
    /**
     * 加载失败并且没有任何数据
     */
    STATUS_NO_NETWORK

}
