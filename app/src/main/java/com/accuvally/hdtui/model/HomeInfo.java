package com.accuvally.hdtui.model;

import java.util.List;

public class HomeInfo {

	private boolean HasRushEvent;// 是否有抢票

	private List<BannerInfo> Banners;// banner 广告条

	private List<BannerInfo> Combos;// 套餐 精选主题

	private List<BannerInfo> Topics;// 专题精选

	private List<SelInfo> Recommends;// 精彩推荐
	
	private List<PoporgsBean> poporgs;// 受欢迎主办方

	public boolean isHasRushEvent() {
		return HasRushEvent;
	}

	public void setHasRushEvent(boolean hasRushEvent) {
		HasRushEvent = hasRushEvent;
	}

	public List<BannerInfo> getBanners() {
		return Banners;
	}

	public void setBanners(List<BannerInfo> banners) {
		Banners = banners;
	}

	public List<BannerInfo> getCombos() {
		return Combos;
	}

	public void setCombos(List<BannerInfo> combos) {
		Combos = combos;
	}

	public List<BannerInfo> getTopics() {
		return Topics;
	}

	public void setTopics(List<BannerInfo> topics) {
		Topics = topics;
	}

	public List<SelInfo> getRecommends() {
		return Recommends;
	}

	public void setRecommends(List<SelInfo> recommends) {
		Recommends = recommends;
	}

	public List<PoporgsBean> getPoporgs() {
		return poporgs;
	}

	public void setPoporgs(List<PoporgsBean> poporgs) {
		this.poporgs = poporgs;
	}

}
