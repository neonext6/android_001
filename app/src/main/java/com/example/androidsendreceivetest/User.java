package com.example.androidsendreceivetest;

import android.graphics.drawable.Drawable;

public class User {
	private Drawable mUserIcon;
	private String mUserName;
	private String mUserPhoneNumber;
	
	User(Drawable userIcon, String userName, String userPhoneNumber){
		mUserIcon = userIcon;
		mUserName = userName;
		mUserPhoneNumber = userPhoneNumber;
	}
	public Drawable getUserIcon() {
		return mUserIcon;
	}
	public String getUserName() {
		return mUserName;
	}
	public String getUserPhoneNumber() {
		return mUserPhoneNumber;
	}
}
