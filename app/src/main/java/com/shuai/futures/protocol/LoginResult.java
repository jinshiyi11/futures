package com.shuai.futures.protocol;

public class LoginResult {
	private boolean mIsLoginSuccess;
	private int mErrorCode;
	private String mErrorMessage;
	
	public LoginResult(){
		mIsLoginSuccess=true;
	}
	
	public LoginResult(int errorCode,String errorMessage){
		mErrorCode=errorCode;
		mErrorMessage=errorMessage;
	}
	
	public boolean isLoginSuccess(){
		return mIsLoginSuccess;
	}

	public int getErrorCode(){
		return mErrorCode;
	}
	
	public String getErrorMessage(){
		return mErrorMessage;
	}
}
