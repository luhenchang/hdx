package com.accuvally.hdtui.ui;

import com.accuvally.hdtui.ui.ViewFlow.ViewSwitchListener;



/**
 * 切换事件
 * 
 * @author wangxiaojie
 */
public interface FlowIndicator extends ViewSwitchListener {

	public void setViewFlow(ViewFlow view);

	public void onScrolled(int h, int v, int oldh, int oldv);
}
