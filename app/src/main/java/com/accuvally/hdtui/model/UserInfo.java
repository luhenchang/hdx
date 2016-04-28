package com.accuvally.hdtui.model;

import java.io.Serializable;

public class UserInfo implements Serializable{

	private String Account;//帐号
	private String Nick;// 昵称

	private String Brief;// 个性签�

	private String City;// 城市

	private String Country;// 国家

	private String Email;// 邮箱

	private boolean EmailActivated;// 邮箱是否已激�

	private int FollowEvents;// 关注的活�

	private int FollowOrgs;// 关注的主办方

	private boolean IsEventCreator;// 是否是活动创建�

	private String Logo;//头像地址

	private String LogoLarge;//大头像地址

	private int MyPubEvents;// 个人发布的活动数�

	private int MyRegEvents;// 已参加的活动数量


	private String OpenIdBaidu;

	private String OpenIdQQ;

	private String OpenIdRenRen;

	private String OpenIdWeibo;

	private String Phone;// 手机号码

	private boolean PhoneActivated;// 是否已激活手�

	private String Province;// 省份

	private String RealName;// 真实姓名

	private int Gender;// 1�0�

	private int Status;// 状�1正常 0已锁�-1已删�

	private boolean ThirdLoginSucess;// 是否是第三方登录

	private int LoginType;// -1-活动行，0-微博�-QQ

	private String id;//long值ID,既不是用户id,也不是设备id

	private String Token;

 

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public int getLoginType() {
		return LoginType;
	}

	public void setLoginType(int loginType) {
		LoginType = loginType;
	}

	public boolean isThirdLoginSucess() {
		return ThirdLoginSucess;
	}

	public void setThirdLoginSucess(boolean thirdLoginSucess) {
		ThirdLoginSucess = thirdLoginSucess;
	}

	public String getAccount() {
		return Account;
	}

	public void setAccount(String account) {
		Account = account;
	}

	public String getBrief() {
		return Brief;
	}

	public void setBrief(String brief) {
		Brief = brief;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public boolean isEmailActivated() {
		return EmailActivated;
	}

	public void setEmailActivated(boolean emailActivated) {
		EmailActivated = emailActivated;
	}

	public int getFollowEvents() {
		return FollowEvents;
	}

	public void setFollowEvents(int followEvents) {
		FollowEvents = followEvents;
	}

	public int getFollowOrgs() {
		return FollowOrgs;
	}

	public void setFollowOrgs(int followOrgs) {
		FollowOrgs = followOrgs;
	}

	public boolean isIsEventCreator() {
		return IsEventCreator;
	}

	public void setIsEventCreator(boolean isEventCreator) {
		IsEventCreator = isEventCreator;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public String getLogoLarge() {
		return LogoLarge;
	}

	public void setLogoLarge(String logoLarge) {
		LogoLarge = logoLarge;
	}

	public int getMyPubEvents() {
		return MyPubEvents;
	}

	public void setMyPubEvents(int myPubEvents) {
		MyPubEvents = myPubEvents;
	}

	public int getMyRegEvents() {
		return MyRegEvents;
	}

	public void setMyRegEvents(int myRegEvents) {
		MyRegEvents = myRegEvents;
	}

	public String getNick() {
		return Nick;
	}

	public void setNick(String nick) {
		Nick = nick;
	}

	public String getOpenIdBaidu() {
		return OpenIdBaidu;
	}

	public void setOpenIdBaidu(String openIdBaidu) {
		OpenIdBaidu = openIdBaidu;
	}

	public String getOpenIdQQ() {
		return OpenIdQQ;
	}

	public void setOpenIdQQ(String openIdQQ) {
		OpenIdQQ = openIdQQ;
	}

	public String getOpenIdRenRen() {
		return OpenIdRenRen;
	}

	public void setOpenIdRenRen(String openIdRenRen) {
		OpenIdRenRen = openIdRenRen;
	}

	public String getOpenIdWeibo() {
		return OpenIdWeibo;
	}

	public void setOpenIdWeibo(String openIdWeibo) {
		OpenIdWeibo = openIdWeibo;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public boolean isPhoneActivated() {
		return PhoneActivated;
	}

	public void setPhoneActivated(boolean phoneActivated) {
		PhoneActivated = phoneActivated;
	}

	public String getProvince() {
		return Province;
	}

	public void setProvince(String province) {
		Province = province;
	}

	public String getRealName() {
		return RealName;
	}

	public void setRealName(String realName) {
		RealName = realName;
	}

	public int getGender() {
		return Gender;
	}

	public void setGender(int gender) {
		Gender = gender;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

}
