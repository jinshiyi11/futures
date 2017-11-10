package com.shuai.futures.protocol;

import com.google.gson.annotations.SerializedName;

/**
 * 登陆成功后返回的数据
 */
public class TokenInfo {
	
	@SerializedName("uid")
	private long mUid;
	
	@SerializedName("token")
	private String mToken;
	
	public long getUid() {
		return mUid;
	}
	public void setUid(long uid) {
		this.mUid = uid;
	}
	public String getToken() {
		return mToken;
	}
	public void setToken(String token) {
		this.mToken = token;
	}

}
