package com.accuvally.hdtui.model;

import java.io.Serializable;

public class QuestionBean implements Serializable {

	private String Q;
	private String A;

	public String getQ() {
		return Q;
	}

	public void setQ(String q) {
		Q = q;
	}

	public String getA() {
		return A;
	}

	public void setA(String a) {
		A = a;
	}
}
