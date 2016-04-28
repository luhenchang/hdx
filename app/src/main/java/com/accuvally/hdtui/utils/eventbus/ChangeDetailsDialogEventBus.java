package com.accuvally.hdtui.utils.eventbus;

import com.accuvally.hdtui.model.RegSuccessInfo;

public class ChangeDetailsDialogEventBus {

	private RegSuccessInfo info;

	public ChangeDetailsDialogEventBus(RegSuccessInfo info) {
		this.info = info;
	}

	public RegSuccessInfo getInfo() {
		return info;
	}

}
