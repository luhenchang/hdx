package com.accuvally.hdtui.model;

import java.io.Serializable;

public class MemberBean implements Serializable {

	public String Account;
	public String Nick;
	public String Logo;

	@Override
	public boolean equals(Object object) {

		if (object != null && object instanceof MemberBean) {
			MemberBean memberBean = (MemberBean) object;
			return this.Account.equals(memberBean.Account);
		}
		return false;
	}
}
