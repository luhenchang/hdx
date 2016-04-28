package com.accuvally.hdtui.model;

import java.io.Serializable;
import java.util.List;

public class QuestionResultBean implements Serializable {
	private String Version;
	private List<QuestionBean> FAQ;

	public String getVersion() {
		return Version;
	}

	public void setVersion(String version) {
		Version = version;
	}

	public List<QuestionBean> getFAQ() {
		return FAQ;
	}

	public void setFAQ(List<QuestionBean> fAQ) {
		FAQ = fAQ;
	}

}
