package com.shuai.futures.data;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.shuai.futures.logic.UserManager;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class UserInfo {
	public final static int  SEX_UNSET=1;
	public final static int  SEX_MAN=2;
	public final static int SEX_WOMAN=3;

	@SerializedName("uid")
	private long mUid;
	
	@SerializedName("weixinId")
	private String mWeixinId;

	@SerializedName("password")
	private String mPassword;
	
	@SerializedName("loginType")
	private int mLoginType= UserManager.NOT_LOGIN;

	private transient String mToken;
	
	@SerializedName("phoneNumber")
	private String mPhoneNumber;
	
	@SerializedName("headImageUrl")
	private String mHeadImageUrl;
	
	@SerializedName("realName")
	private String mRealName;
	
	@SerializedName("nickName")
	private String mNickName;
	
	/**
	 * 身份证号
	 */
	@SerializedName("idcard")
	private String mIdCard;
	
	/**
	 * 出生日期
	 */
	@SerializedName("birthday")
	private long mBirthday;
	
	/**
	 * 家庭住址
	 */
	@SerializedName("address")
	private String mAddress;
	
	/**
	 * 性别
	 */
	@SerializedName("sex")
	private int mSex=SEX_UNSET;
	
	/**
	 * 账户余额,单位元
	 */
	@SerializedName("rcoin")
	private float mBalance;
	
	/**
	 * 我的金币
	 */
	@SerializedName("vcoin")
	private float mCoin;
	
	/**
	 * 绑定的银行卡号
	 */
	@SerializedName("bank_card")
	private String mBankCard;
	
	/**
	 * 绑定的银行卡持卡人名称
	 */
	@SerializedName("bank_user_name")
	private String mBankUserName;
	
	/**
	 * 绑定的银行卡的开户银行，如招商银行
	 */
	@SerializedName("bank_name")
	private String mBankName;
	
	/**
	 * 绑定的银行卡的开户支行信息
	 */
	@SerializedName("bank_address")
	private String mBankAddress;

	public UserInfo() {
	}

	public long getUid() {
		return mUid;
	}

	public void setUid(long uid) {
		this.mUid = uid;
	}
	
	public String getWeixinId() {
		return mWeixinId;
	}

	public void setWeixinId(String weixinId) {
		this.mWeixinId = weixinId;
	}

	public String getPassword() {
		return mPassword;
	}

	public void setPassword(String password) {
		this.mPassword = password;
	}

	public String getToken() {
		return mToken;
	}

	public void setToken(String token) {
		this.mToken = token;
	}
	
	public int getLoginType(){
		return mLoginType;
	}

	public void setLoginType(int loginType){
		mLoginType=loginType;
	}
	
	public String getPhoneNumber(){
		return mPhoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber){
		mPhoneNumber=phoneNumber;
	}

	public String getRealName() {
		return mRealName;
	}

	public void setRealName(String realName) {
		this.mRealName = realName;
	}

	public String getNickName() {
		return mNickName;
	}

	public void setNickName(String nickName) {
		this.mNickName = nickName;
	}

	public String getHeadImageUrl() {
		return mHeadImageUrl;
	}

	public void setHeadImageUrl(String headImageUrl) {
		this.mHeadImageUrl = headImageUrl;
	}

	public String getIdCard() {
		return mIdCard;
	}

	public void setIdCard(String idCard) {
		this.mIdCard = idCard;
	}

	public long getBirthday() {
		return mBirthday;
	}

	public void setBirthday(long birthday) {
		this.mBirthday = birthday;
	}
	
	public String getBirthdayString(){
		if(mBirthday<=0)
			return "";
		
		Date date=new Date(mBirthday);
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		return dateFormat.format(date);
	}

	public int getSex() {
		return mSex;
	}

	public void setSex(int sex) {
		this.mSex = sex;
	}
	
	public String getSexString() {
		String result="";
		if(mSex==SEX_MAN)
			result="男";
		else if(mSex==SEX_WOMAN)
			result="女";
		return result;
	}
	
	public static int parseSexString(String sex) {
		int result = SEX_UNSET;
		if (!TextUtils.isEmpty(sex)) {
			if (sex.equals("男"))
				result = SEX_MAN;
			else if (sex.equals("女"))
				result = SEX_WOMAN;
		}
		return result;
	}

	public String getAddress() {
		return mAddress;
	}

	public void setAddress(String address) {
		this.mAddress = address;
	}

	public float getBalance() {
		return mBalance;
	}

	public void setBalance(float balance) {
		this.mBalance = balance;
	}

	public float getCoin() {
		return mCoin;
	}

	public void setCoin(float coin) {
		this.mCoin = coin;
	}

	public String getBankCard() {
		return mBankCard;
	}
	
	public boolean isBankCardBinded(){
		return !TextUtils.isEmpty(mBankCard);
	}

	public void setBankCard(String bankCard) {
		this.mBankCard = bankCard;
	}
	
	public String getBankAddress() {
		return mBankAddress;
	}
	
	public void setBankAddress(String bankAddress) {
		this.mBankAddress = bankAddress;
	}
	
	public String getBankName() {
		return mBankName;
	}
	
	public void setBankName(String bankName) {
		this.mBankName = bankName;
	}
	
	public String getBankUserName() {
		return mBankUserName;
	}
	
	public void setBankUserName(String bankUserName) {
		this.mBankUserName = bankUserName;
	}
	
}
