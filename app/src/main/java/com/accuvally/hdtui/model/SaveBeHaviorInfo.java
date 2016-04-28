package com.accuvally.hdtui.model;

import android.util.Log;

import com.avos.avoscloud.LogUtil.log;

public class SaveBeHaviorInfo {

	private String uid;
	private String tid;
	private String type;
	private String target_type;
	private String target_id;
	private String data_source;
	private String location;
	private String country;
	private String province;
	private String city;
	private String date_time;
	private String key_word;
	private String device_type;
	private String tag;
	private String version;
	private String event_name;
	private String event_data;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getEvent_name() {
		return event_name;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	public String getEvent_data() {
		return event_data;
	}

	public void setEvent_data(String event_data) {
		this.event_data = event_data;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTarget_type() {
		return target_type;
	}

	public void setTarget_type(String target_type) {
		this.target_type = target_type;
	}

	public String getTarget_id() {
		return target_id;
	}

	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}

	public String getData_source() {
		return data_source;
	}

	public void setData_source(String data_source) {
		this.data_source = data_source;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDate_time() {
		return date_time;
	}

	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}

	public String getKey_word() {
		return key_word;
	}

	public void setKey_word(String key_word) {
		this.key_word = key_word;
	}

	public String getDevice_type() {
		return device_type;
	}

	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}

	@Override
	public String toString() {
		StringBuilder json = new StringBuilder();
		json.append("{\"uid\":\"" + uid + "\",");
		json.append("\"tid\":\"" + tid + "\",");
		json.append("\"type\":\"" + type + "\",");
		json.append("\"target_type\":\"" + target_type + "\",");
		json.append("\"target_id\":\"" + target_id + "\",");
		json.append("\"data_source\":\"" + data_source + "\",");
		json.append("\"location\":\"" + location + "\",");
		json.append("\"country\":\"" + country + "\",");
		json.append("\"province\":\"" + province + "\",");
		json.append("\"city\":\"" + city + "\",");
		json.append("\"date_time\":\"" + date_time + "\",");
		json.append("\"key_word\":\"" + key_word + "\",");
		json.append("\"tag\":\"" + tag + "\",");
		json.append("\"version\":\"" + version + "\",");
		json.append("\"event_name\":\"" + event_name + "\",");
		json.append("\"event_data\":\"" + event_data + "\",");
		json.append("\"device_type\":\"" + device_type + "\"}");
		return json.toString();
	}

}
