package com.accuvally.hdtui.model;

import java.io.Serializable;

public class GeTuiNotification implements Serializable {

	public String title;
	public String content;
	public String logo_url;

	public int op_type;
	public String op_value;

	public String expired_time;
	public String template_type;
	public String init_time;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

	public int getOp_type() {
		return op_type;
	}

	public void setOp_type(int op_type) {
		this.op_type = op_type;
	}

	public String getOp_value() {
		return op_value;
	}

	public void setOp_value(String op_value) {
		this.op_value = op_value;
	}

	public String getExpired_time() {
		return expired_time;
	}

	public void setExpired_time(String expired_time) {
		this.expired_time = expired_time;
	}

	public String getTemplate_type() {
		return template_type;
	}

	public void setTemplate_type(String template_type) {
		this.template_type = template_type;
	}

	public String getInit_time() {
		return init_time;
	}

	public void setInit_time(String init_time) {
		this.init_time = init_time;
	}

}
