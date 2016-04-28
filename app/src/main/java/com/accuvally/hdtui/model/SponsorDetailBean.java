package com.accuvally.hdtui.model;

import java.io.Serializable;
import java.util.List;

public class SponsorDetailBean implements Serializable {

	public SponsorDetailOrg org;
	public List<SelInfo> futures;
	public List<SelInfo> olds;
}

//	public class PastActsBean implements Serializable {
//		public String Id;
//
//		public int Max;
//
//		public boolean IsQing;// 是否是轻活动
//
//		public String Title;// title
//
//		public String Address;// 地址
//
//		public String Start;// 开始时间
//
//		public String End;// 结束时间
//
//		public String LogoUrl;// 图片地址
//
//		public int LikeNum;// 喜欢
//
//		public String Url;
//
//		public boolean IsFollow;
//
//		public int SourceType;
//
//		public String ShareUrl;
//	}
//
//	public class FutureActsBean implements Serializable {
//		public String Id;
//
//		public int Max;
//
//		public boolean IsQing;// 是否是轻活动
//		public String Title;
//		public String ShareUrl;
//		public boolean IsFollow;
//		public int LikeNum;
//
//		public String Address;// 地址
//
//		public String Start;// 开始时间
//
//		public String End;// 结束时间
//
//		public int SourceType;
//
//		public int EventNum;
//		public int Follows;
//	}
//}