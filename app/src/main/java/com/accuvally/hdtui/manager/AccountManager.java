package com.accuvally.hdtui.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.accuvally.hdtui.AccuApplication;
import com.accuvally.hdtui.config.Config;
import com.accuvally.hdtui.model.UserInfo;

public class AccountManager {

	private static AccuApplication accuApp = AccuApplication.getInstance();
	private static volatile AccountManager accountManager;
	
	private AccountManager() {};
	
	public static AccountManager getInstance() {
		if (accountManager == null) {
			synchronized (AccountManager.class) {
				if (accountManager == null) {
					accountManager = new AccountManager();
				}
			}
		}
		return accountManager;
	}
	
	private static UserInfo curUserInfo;
	
	public static String getAccount() {
		return getUserInfo().getAccount();
	}
	
	public static String getNick() {
		return getUserInfo().getNick();
	}


    public static String getPhone() {
        return getUserInfo().getPhone();
    }
	
	public static void logout() {
		curUserInfo = null;
	}
	
	public static boolean checkIsLogin() {
		return !TextUtils.isEmpty(getUserInfo().getAccount());
	}
	
	public static UserInfo getUserInfo() {
		return accuApp.getUserInfo();
	}

	public static void setUserInfo(UserInfo userInfo) {
		if (userInfo == null) return;
		
		SharedPreferences sp = AccuApplication.getInstance().getSharedPreferences(Config.PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("Account", userInfo.getAccount());
		editor.putString("Brief", userInfo.getBrief());
		editor.putString("City", userInfo.getCity());
		editor.putString("Country", userInfo.getCountry());
		editor.putString("Email", userInfo.getEmail());
		editor.putBoolean("EmailActivated", userInfo.isEmailActivated());
		editor.putInt("FollowEvents", userInfo.getFollowEvents());
		editor.putInt("FollowOrgs", userInfo.getFollowOrgs());
		editor.putBoolean("IsEventCreator", userInfo.isIsEventCreator());
		editor.putString("Logo", userInfo.getLogo());
		editor.putString("LogoLarge", userInfo.getLogoLarge());
		editor.putInt("MyPubEvents", userInfo.getMyPubEvents());
		editor.putInt("MyRegEvents", userInfo.getMyRegEvents());
		editor.putString("Nick", userInfo.getNick());
		editor.putString("OpenIdBaidu", userInfo.getOpenIdBaidu());
		editor.putString("OpenIdQQ", userInfo.getOpenIdQQ());
		editor.putString("OpenIdRenRen", userInfo.getOpenIdRenRen());
		editor.putString("OpenIdWeibo", userInfo.getOpenIdWeibo());
		editor.putString("Phone", userInfo.getPhone());
		editor.putBoolean("PhoneActivated", userInfo.isPhoneActivated());
		editor.putString("Province", userInfo.getProvince());
		editor.putString("RealName", userInfo.getRealName());
		editor.putInt("Gender", userInfo.getGender());
		editor.putInt("Status", userInfo.getStatus());
		editor.putBoolean("ThirdLoginSucess", userInfo.isThirdLoginSucess());
		editor.putInt("LoginType", userInfo.getLoginType());
		editor.commit();
		
		curUserInfo = userInfo;
	}

	public static boolean isActivatedAccount() {
		UserInfo userInfo = getUserInfo();
		return userInfo.isPhoneActivated() || userInfo.isEmailActivated();
	}
}
