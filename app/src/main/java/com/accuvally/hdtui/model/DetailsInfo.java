package com.accuvally.hdtui.model;//package com.accuvally.hdtui.model;
//
//import java.io.Serializable;
//import java.util.List;
///**
// * 
//{
//    "Id": 5255120108800,
//    "Title": "APEC期间【单身交友】主题活动",
//    "Addr": "东城东直门东环广场负一层约吧",
//    "Start": "2014-11-07 18:30",
//    "End": "2014-12-31 21:30",
//    "LikeNum": 0, -- 喜欢数量
//    "Status": 4, -- 活动状态  共以下4种：
//                                   0报名尚未开始
//                                   1报名时间已截止
//                                   2活动名额已满
//                                   3活动已成功举办
//                                   4活动进行中
//                                   5活动已取消
//
//
//    "Logo": "",
//    "Createby": "5491725574264",   -- 创建人帐号id
//    "City": "东城",
//    "Max": 100,                    
//    "Num": 0,                      -- 报名数量
//    "Creator": "leping999",        -- 创建人
//    "Comments": 0,                 -- 评论数量
//    "Desc": "<p>放假啦，去哪？ ",  -- 描述
//    "ShowDisabledTicket": true,    -- 是否限时无效票券
//    "Category": 0,                 -- 
//    "CreatorLogo": "",             -- 创建人Logo
//    "Template":                    --  报名模版
//               "{
//                  \"max\": \"2\",                -- 模版个数 
//                  \"Version\": \"1\",            -- 版本
//                  \"Item\": [ -- 表单类型有以下：
//                                   1．input  单行文本输入框
//                                   2．checkbox  多选
//                                   3．radio 单选
//                                   4．textarea  多行文本输入框
//                                   5．select 单选 
//                                   6. number 数字输入框 
//                                   7. date     日期选择框
//                                   8. email    邮箱
//							        {
//							            \"Sort\": \"1\",       -- 排序
//							            \"Type\": \"input\",   -- 类型
//							            \"Required\": true,    -- 必备字段
//							            \"Title\"公司\"        
//							        },
//							        {
//							            \"Sort\": \"2\",
//							            \"Type\": \"radio\",
//							            \"Required\": true,
//							            \"Title\": \"学历\",
//							            \"SubItems\": [
//							                \"小学\",
//							                \"中学\",
//							                \"大专\",
//							                \"本科\",
//							                \"研究生以上\"
//							            ]
//							        },…
//							    ]
//		}",
// "Interest": [                  -- 感兴趣的活动  10 条数据
//        {
//            "Max": 100,
//            "IsQing": false,
//            "Id": 8250035855500,
//            "Title": "十一假期【全城热恋】七零八零后单身交友专场活动",
//            "ShareUrl": "http://192.168.0.107/event/8250035855500",
//            "IsFollow": false,
//            "LikeNum": 45,
//            "Address": "东直门东环广场及魏公村韦伯大厦",
//            "Start": "2014-10-03 18:30",
//            "End": "2014-11-30 21:30",
//            "SourceType": 0,
//            "Url": "http://192.168.0.107/event/8250035855500",
//            "LogoUrl": "http://192.168.0.107/logo/201410/8250035855500/981736362504453.jpg"
//        },……
//    ],
//    "Cmts": [                                 -- 评论 数组
//        {
//            "Rid": 15339879897264982,         -- 回复人id
//            "Id": 15339879889999982,          -- 评论人id
//            "CreateDate": "2014-11-11 10:46", -- 评论时间
//            "Content": "回复@44：yeah.",      -- 内容
//              "Logo":                           -- 评论人logo
//"http://192.168.0.107/logo/user/201409/0102/5601704623258/201770435156231.jpg",
//            "Nick": "44"                      -- 评论人昵称
//        }, ……
//    ],
//    "DetailExtern": [    -- 活动扩展内容  
//   {
// "Title":"",
//"Content":""
//} ,……
//    ],
//    "ShareUrl": "http://192.168.0.107/event/5255120108800", -- 分享地址
//    "Ticks": [           -- 票券
//           {
//            "SN": 1,
//            "Status": 4,  -- 票券状态  共以下6种：
//									0未开始购买
//									1购买时间已截止
//									2已暂停销售
//									3有效期已过
//									4可报名
//									5已售罄
//            "Price": 0.0,               --  票价
//            "Currency": "RMB", 
//            "Title": "免费票",
//            "Desc": "免费票说明：特价", -- 票券描述
//            "Quantity": 0,              -- 可购买票的总数
//            "SoldNum": 2,               -- 已售出数量
//            "BookNum": 0,               -- 已预定数量
//            "BookStart": "",     -- 预定开始时间
//            "BookEnd": "" ,      -- 预定结束时间
//            "QuantityUnit": 1,   -- 几人票
//            "MinOrder": 1,       -- 最少预定数量
//            "MaxOrder": 1,       -- 最多预定数量
//            "NeedApply": false,  -- 购票是否需要审核
//            "EffectDate": "",    -- 此類票券的允許使用時間
//            "ExpiredDate": "",   -- 购票截止时间
//            "Group": "",
//            "Enabled": true,     -- 票券是否可用
//            "StatusStr": "报名中",  -- 票券状态字符串
//            "PriceStr": "免费"      -- 票价字符串
//            "HadReg":true,          -- 用户是否已报名该票种
//            "Msg": "您已报名该票种，请勿重新报名！"      -- 
//        },……
//    ],
//"Images":              -- 活动相册
//[
//        {
//            "Title": "",
//            "Comment": "",
//            "Url": "http://cdn.huodongxing.com/logo/201412/151824399915090.jpg",
//            "SN": "4"
//        },……
//],
//    "Hasfollowed": false,  -- 是否已关注
//    "Tags": [              -- 标签
//        "聚会",”创业”,……
//    ],
//    "Summary": "….",       -- 摘要
//    "OrgLogo": "",         --  主办方Logo
//    "OrgName": "",         --  主办方名称
//    "OrgID": 0             --  主办方id  
//	"VisitNum": "817",     --  查看数  
//	“RemainingTimeStr”:” 即将开始” 
//	"HasCoupon": false,   --  是否有优惠码
//}	
// */
//public class DetailsInfo implements Serializable {
//	private String Id;
//
//	private String Title;
//
//	private String Addr;
//
//	private String Start;
//
//	private String End;
//	
//	private String TimeStr;//新加字段  活动时间;
//
//	private int LikeNum;
//
//	private int Status;
//
//	private String Logo;
//
//	private String Createby;// 創建人id
//
//	private String City;
//
//	private int Max;
//
//	private int Num;
//
//	private String Creator;// 創建人
//
//	private String Desc;
//
//	private boolean ShowDisabledTicket;
//
//	private int Category;
//
//	private String CreatorLogo;
//
//	private String Template;
//
//	private String ShareUrl;
//
//	private boolean Hasfollowed;
//
//	private String Summary;
//
//	private String OrgLogo;
//
//	private String OrgName;
//
//	private String OrgID;
//
//	private String RemainingTimeStr;
//	
//	private boolean HasCoupon;//是否有优惠码
//
//	private String RemindNotes;//公布名单时间
//	
//	private List<DetailsTicketInfo> Ticks;
//	
//	private boolean IsRush;
//
//	private double MinPrice;
//	private double MaxPrice;
//	private String VisitNum;
//	
//	private String RushStatus;//抢票活动用的抢票状态
//
//	public String getRushStatus() {
//		return RushStatus;
//	}
//
//	public void setRushStatus(String rushStatus) {
//		RushStatus = rushStatus;
//	}
//
//	public boolean isIsRush() {
//		return IsRush;
//	}
//
//	public void setIsRush(boolean isRush) {
//		IsRush = isRush;
//	}
//
//	public String getVisitNum() {
//		return VisitNum;
//	}
//
//	public void setVisitNum(String visitNum) {
//		VisitNum = visitNum;
//	}
//
//	public double getMinPrice() {
//		return MinPrice;
//	}
//
//	public void setMinPrice(double minPrice) {
//		MinPrice = minPrice;
//	}
//
//	public double getMaxPrice() {
//		return MaxPrice;
//	}
//
//	public void setMaxPrice(double maxPrice) {
//		MaxPrice = maxPrice;
//	}
//
//	public String getId() {
//		return Id;
//	}
//
//	public void setId(String id) {
//		Id = id;
//	}
//
//	public String getTitle() {
//		return Title;
//	}
//
//	public void setTitle(String title) {
//		Title = title;
//	}
//
//	public String getAddr() {
//		return Addr;
//	}
//
//	public void setAddr(String addr) {
//		Addr = addr;
//	}
//
//	public String getStart() {
//		return Start;
//	}
//
//	public void setStart(String start) {
//		Start = start;
//	}
//
//	public String getEnd() {
//		return End;
//	}
//
//	public void setEnd(String end) {
//		End = end;
//	}
//
//	public String getTimeStr() {
//		return TimeStr;
//	}
//
//	public void setTimeStr(String timeStr) {
//		TimeStr = timeStr;
//	}
//
//	public int getLikeNum() {
//		return LikeNum;
//	}
//
//	public void setLikeNum(int likeNum) {
//		LikeNum = likeNum;
//	}
//
//	public int getStatus() {
//		return Status;
//	}
//
//	public void setStatus(int status) {
//		Status = status;
//	}
//
//	public String getLogo() {
//		return Logo;
//	}
//
//	public void setLogo(String logo) {
//		Logo = logo;
//	}
//
//	public String getCreateby() {
//		return Createby;
//	}
//
//	public void setCreateby(String createby) {
//		Createby = createby;
//	}
//
//	public String getCity() {
//		return City;
//	}
//
//	public void setCity(String city) {
//		City = city;
//	}
//
//	public int getMax() {
//		return Max;
//	}
//
//	public void setMax(int max) {
//		Max = max;
//	}
//
//	public int getNum() {
//		return Num;
//	}
//
//	public void setNum(int num) {
//		Num = num;
//	}
//
//	public String getCreator() {
//		return Creator;
//	}
//
//	public void setCreator(String creator) {
//		Creator = creator;
//	}
//
//	public String getDesc() {
//		return Desc;
//	}
//
//	public void setDesc(String desc) {
//		Desc = desc;
//	}
//
//	public boolean isShowDisabledTicket() {
//		return ShowDisabledTicket;
//	}
//
//	public void setShowDisabledTicket(boolean showDisabledTicket) {
//		ShowDisabledTicket = showDisabledTicket;
//	}
//
//	public int getCategory() {
//		return Category;
//	}
//
//	public void setCategory(int category) {
//		Category = category;
//	}
//
//	public String getCreatorLogo() {
//		return CreatorLogo;
//	}
//
//	public void setCreatorLogo(String creatorLogo) {
//		CreatorLogo = creatorLogo;
//	}
//
//	public String getTemplate() {
//		return Template;
//	}
//
//	public void setTemplate(String template) {
//		Template = template;
//	}
//
//	public String getShareUrl() {
//		return ShareUrl;
//	}
//
//	public void setShareUrl(String shareUrl) {
//		ShareUrl = shareUrl;
//	}
//
//	public boolean isHasfollowed() {
//		return Hasfollowed;
//	}
//
//	public void setHasfollowed(boolean hasfollowed) {
//		Hasfollowed = hasfollowed;
//	}
//
//	public String getSummary() {
//		return Summary;
//	}
//
//	public void setSummary(String summary) {
//		Summary = summary;
//	}
//
//	public String getOrgLogo() {
//		return OrgLogo;
//	}
//
//	public void setOrgLogo(String orgLogo) {
//		OrgLogo = orgLogo;
//	}
//
//	public String getOrgName() {
//		return OrgName;
//	}
//
//	public void setOrgName(String orgName) {
//		OrgName = orgName;
//	}
//
//	public String getOrgID() {
//		return OrgID;
//	}
//
//	public void setOrgID(String orgID) {
//		OrgID = orgID;
//	}
//
//	public String getRemainingTimeStr() {
//		return RemainingTimeStr;
//	}
//
//	public void setRemainingTimeStr(String remainingTimeStr) {
//		RemainingTimeStr = remainingTimeStr;
//	}
//
//	public List<DetailsTicketInfo> getTicks() {
//		return Ticks;
//	}
//
//	public void setTicks(List<DetailsTicketInfo> ticks) {
//		Ticks = ticks;
//	}
//
//	public boolean isHasCoupon() {
//		return HasCoupon;
//	}
//
//	public void setHasCoupon(boolean hasCoupon) {
//		HasCoupon = hasCoupon;
//	}
//
//	public String getRemindNotes() {
//		return RemindNotes;
//	}
//
//	public void setRemindNotes(String remindNotes) {
//		RemindNotes = remindNotes;
//	}
//}
