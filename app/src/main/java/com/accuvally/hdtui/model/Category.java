package com.accuvally.hdtui.model;

import java.util.ArrayList;
import java.util.List;

public class Category {

	private String mCategoryName;
	private List<HomeEventInfo> mCategoryItem = new ArrayList<HomeEventInfo>();

	public List<HomeEventInfo> getmCategoryItem() {
		return mCategoryItem;
	}

	public void setmCategoryItem(List<HomeEventInfo> mCategoryItem) {
		this.mCategoryItem = mCategoryItem;
	}

	public void setmCategoryName(String mCategoryName) {
		this.mCategoryName = mCategoryName;
	}

	public Category(String mCategroyName) {
		mCategoryName = mCategroyName;
	}

	public String getmCategoryName() {
		return mCategoryName;
	}

	public void addItem(HomeEventInfo homeEventInfo) {
		mCategoryItem.add(homeEventInfo);
	}

	/**
	 * 获取Item内容
	 * 
	 * @param pPosition
	 * @return
	 */
	public Object getItem(int pPosition) {
		// Category排在第一位
		if (pPosition == 0) {
			return mCategoryName;
		} else {
			return mCategoryItem.get(pPosition - 1);
		}
	}

	/**
	 * 当前类别Item总数。Category也需要占用一个Item
	 * 
	 * @return
	 */
	public int getItemCount() {
		return mCategoryItem.size() + 1;
	}

	public int getChildItemCount() {
		return mCategoryItem.size();
	}
}