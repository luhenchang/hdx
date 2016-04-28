package com.accuvally.hdtui.model;

import java.io.Serializable;
import java.util.List;

/**
 * 活动详情
 * 
 * @author Semmer Wang
 * 
 */
public class HomeDetailsInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String Id;

	private String Title;

	private String Addr;

	private String Start;

	private String End;

	private int LikeNum;

	private int Status;

	private String Logo;

	private String Createby;

	private String City;

	private int Max;

	private int Num;

	private String Creator;

	private String Murl;

	private int Comments;

	private String Desc;

	private boolean ShowDisabledTicket;

	private String Summary;

	private int Category;

	private boolean Hasfollowed;

	private String Location;

	private List<String> Tags;

	private List<CommentInfo> Cmts;

	private List<DetailsTicketInfo> Ticks;

	private List<HomeEventInfo> Interest;

	private String CreatorLogo;

	private String OrgID;

	private String OrgName;

	private String OrgDesc;

	private List<DetailExtern> DetailExtern;
	private String RemainingTimeStr;
	private double MinPrice;
	private double MaxPrice;

	private String VisitNum;// 浏览次数

	public String getVisitNum() {
		return VisitNum;
	}

	public void setVisitNum(String visitNum) {
		VisitNum = visitNum;
	}

	public String getRemainingTimeStr() {
		return RemainingTimeStr;
	}

	public void setRemainingTimeStr(String remainingTimeStr) {
		RemainingTimeStr = remainingTimeStr;
	}

	public double getMinPrice() {
		return MinPrice;
	}

	public void setMinPrice(double minPrice) {
		MinPrice = minPrice;
	}

	public double getMaxPrice() {
		return MaxPrice;
	}

	public void setMaxPrice(double maxPrice) {
		MaxPrice = maxPrice;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<DetailExtern> getDetailExtern() {
		return DetailExtern;
	}

	public void setDetailExtern(List<DetailExtern> detailExtern) {
		DetailExtern = detailExtern;
	}

	public String getOrgDesc() {
		return OrgDesc;
	}

	public void setOrgDesc(String orgDesc) {
		OrgDesc = orgDesc;
	}

	private String OrgLogo;

	private String ShareUrl;

	private String Template;

	public String getTemplate() {
		return Template;
	}

	public void setTemplate(String template) {
		Template = template;
	}

	public String getShareUrl() {
		return ShareUrl;
	}

	public void setShareUrl(String shareUrl) {
		ShareUrl = shareUrl;
	}

	public String getCreatorLogo() {
		return CreatorLogo;
	}

	public void setCreatorLogo(String creatorLogo) {
		CreatorLogo = creatorLogo;
	}

	public String getOrgID() {
		return OrgID;
	}

	public void setOrgID(String orgID) {
		OrgID = orgID;
	}

	public String getOrgName() {
		return OrgName;
	}

	public void setOrgName(String orgName) {
		OrgName = orgName;
	}

	public String getOrgLogo() {
		return OrgLogo;
	}

	public void setOrgLogo(String orgLogo) {
		OrgLogo = orgLogo;
	}

	public String getSummary() {
		return Summary;
	}

	public void setSummary(String summary) {
		Summary = summary;
	}

	public boolean isShowDisabledTicket() {
		return ShowDisabledTicket;
	}

	public void setShowDisabledTicket(boolean showDisabledTicket) {
		ShowDisabledTicket = showDisabledTicket;
	}

	public int getCategory() {
		return Category;
	}

	public void setCategory(int category) {
		Category = category;
	}

	public boolean isHasfollowed() {
		return Hasfollowed;
	}

	public void setHasfollowed(boolean hasfollowed) {
		Hasfollowed = hasfollowed;
	}

	public String getLocation() {
		return Location;
	}

	public void setLocation(String location) {
		Location = location;
	}

	public List<String> getTags() {
		return Tags;
	}

	public void setTags(List<String> tags) {
		Tags = tags;
	}

	public List<CommentInfo> getCmts() {
		return Cmts;
	}

	public void setCmts(List<CommentInfo> cmts) {
		Cmts = cmts;
	}

	public List<DetailsTicketInfo> getTicks() {
		return Ticks;
	}

	public void setTicks(List<DetailsTicketInfo> ticks) {
		Ticks = ticks;
	}

	public List<HomeEventInfo> getInterest() {
		return Interest;
	}

	public void setInterest(List<HomeEventInfo> interest) {
		Interest = interest;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getAddr() {
		return Addr;
	}

	public void setAddr(String addr) {
		Addr = addr;
	}

	public String getStart() {
		return Start;
	}

	public void setStart(String start) {
		Start = start;
	}

	public String getEnd() {
		return End;
	}

	public void setEnd(String end) {
		End = end;
	}

	public int getLikeNum() {
		return LikeNum;
	}

	public void setLikeNum(int likeNum) {
		LikeNum = likeNum;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public String getCreateby() {
		return Createby;
	}

	public void setCreateby(String createby) {
		Createby = createby;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public int getMax() {
		return Max;
	}

	public void setMax(int max) {
		Max = max;
	}

	public int getNum() {
		return Num;
	}

	public void setNum(int num) {
		Num = num;
	}

	public String getCreator() {
		return Creator;
	}

	public void setCreator(String creator) {
		Creator = creator;
	}

	public String getMurl() {
		return Murl;
	}

	public void setMurl(String murl) {
		Murl = murl;
	}

	public int getComments() {
		return Comments;
	}

	public void setComments(int comments) {
		Comments = comments;
	}

	public String getDesc() {
		return Desc;
	}

	public void setDesc(String desc) {
		Desc = desc;
	}

}
