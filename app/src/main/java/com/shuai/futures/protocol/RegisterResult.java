package com.shuai.futures.protocol;

import com.google.gson.annotations.SerializedName;

/**
 * 注册成功后返回的数据
 */
public class RegisterResult {
	
	@SerializedName("uid")
	private String mUid;
	
	@SerializedName("token")
	private String mToken;
	
	@SerializedName("password")
	private String mPassword;
	
	public String getUid() {
		return mUid;
	}
	public void setUid(String uid) {
		this.mUid = uid;
	}
	public String getToken() {
		return mToken;
	}
	public void setToken(String token) {
		this.mToken = token;
	}
	
	public String getPassword() {
		return mPassword;
	}
	public void setPassword(String password) {
		this.mPassword = password;
	}

}
