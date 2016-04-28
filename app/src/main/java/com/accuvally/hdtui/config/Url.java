package com.accuvally.hdtui.config;

public class Url {

	public static final String ACCUPASS_SERVICE_URL2 = "http://api.huodongxing.com/v3";
	public static final String ACCUPASS_SERVICE_URL3 = "https://api.huodongxing.com/v3";

	public static final String ACCUPASS_SERVICE_HOST = "api.huodongxing.com";
	public static final int ACCUPASS_SERVICE_PORT = 80;
	public static final int ACCUPASS_SERVICE_PORT2 = 443;

	public static final Boolean ISDEBUG = false;

	public static final String ACCUPASS_BETA = "ACCUPASS_BETA";
	
	

	/** 获取闪屏图片 **/
	public static final String GET_DYNAMIC_LOGO = ACCUPASS_SERVICE_URL2 + "/flash";
	
	/**意见反馈 **/
	public static final String ACCUPASS_FEEDBACK = ACCUPASS_SERVICE_URL2 + "/comment/sug";
	
	/** 通知IM-@人、发送消息之后调用 **/
	public static final String ACCUPASS_SEND_IM_EVENT = ACCUPASS_SERVICE_URL2 + "/social/send";
	
	/** 上传个推ID **/
	public static final String ACCUPASS_UPDATE_GETUIID = ACCUPASS_SERVICE_URL3 + "/auth/upd_getuiid";

	/** 注册设备 **/
	public static final String ACCUPASS_REGDEVICE = ACCUPASS_SERVICE_URL3 + "/auth/reg_device";
	
	/** 标签Tag获取 **/
	public static final String ACCUPASS_TAG = ACCUPASS_SERVICE_URL2 + "/tag/get_prequery_tags";
	
	/** 记录用户行为 **/
	public static final String SAVE_BEHAVIOR_INFO = ACCUPASS_SERVICE_URL3 + "/behavior/save";
	
	/** 更新用户资料 **/
	public static final String ACCUPASS_UPDATE_USERINFO = ACCUPASS_SERVICE_URL2 + "/user/updprofile";
	
	/** 注册 **/
	public static final String ACCUPASS_USER_REGISTER = ACCUPASS_SERVICE_URL3 + "/auth/register";

	/** 第三方登录 **/
	public static final String ACCUPASS_THREE_LOGIN = ACCUPASS_SERVICE_URL3 + "/auth/3login";

	/** 获取验证码 **/
	public static final String ACCUPASS_SEND_CODE = ACCUPASS_SERVICE_URL3 + "/auth/send_code";
	
	/** 修改密码 **/
	public static final String ACCUPASS_UPDATE_PASS = ACCUPASS_SERVICE_URL2 + "/user/updpwd";
	
	/** 未完成票券**/
	public static final String MYTICKETS_UNFINISHED = ACCUPASS_SERVICE_URL2 + "/tickets/undones";
	
	/** 我的票券 **/
	public static final String ACCUPASS_MYATTEND = ACCUPASS_SERVICE_URL2 + "/act/had_attend";
	
	/** 校验优惠码 **/
	public static final String ACCUPASS_VALIDATE_COUPON = ACCUPASS_SERVICE_URL2 + "/tickets/validate_coupon";
	
	/** 绑定邮箱或手机 **/
	public static final String ACCUPASS_VALICODE = ACCUPASS_SERVICE_URL3 + "/auth/valid_code";
	
	/** 确认订单页面——进入支付页面 **/
	public static final String PAY = ACCUPASS_SERVICE_URL2 + "/payment/check_out";
	
	/** 确认订单页面——获取订单信息 **/
	public static final String PAY_DETAILS = ACCUPASS_SERVICE_URL2 + "/payment/get_payment_info";
	
	/** 上传头像 **/
	public static final String ACCUPASS_UPLOAD_HEAD = ACCUPASS_SERVICE_URL2 + "/user/upload_user_logo";
	
	
	
	
	
	/** 首页推荐 **/
	public static final String HOME_URL = ACCUPASS_SERVICE_URL2 + "/search/home_data";
	
	/** 精彩推荐 **/
	public static final String search_recommend = ACCUPASS_SERVICE_URL2 + "/search/recommend";
	
	/** 查询活动——寻找 **/
	public static final String ACCUPASS_HOME_TAB = ACCUPASS_SERVICE_URL2 + "/search/query_events";
	
	/** 活动详情收藏 **/
	public static final String ACCUPASS_HOME_FOLLOW = ACCUPASS_SERVICE_URL2 + "/act/follow";
	
	/** 活动详情取消收藏 **/
	public static final String ACCUPASS_HOME_UNFOLLOW = ACCUPASS_SERVICE_URL2 + "/act/unfollow";
	
	/** 获取活动详情 **/
	public static final String ACCUPASS_HOME_DETAIL = ACCUPASS_SERVICE_URL2 + "/act";
	
	/** 已关注的主办方列表 **/
	public static final String ACCUPASS_MYFOLLOW = ACCUPASS_SERVICE_URL2 + "/org/followed";
	
	/** 主办方信息 **/
	public static final String ACCUPASS_ORGANIZER_DETAILS = ACCUPASS_SERVICE_URL2 + "/org";
	
	/** 关注主办方 **/
	public static final String ORG_FOLLOW = ACCUPASS_SERVICE_URL2 + "/org/follow";
	
	/** 取消关注主办方 **/
	public static final String ORG_UNFOLLOW = ACCUPASS_SERVICE_URL2 + "/org/unfollow";
	
	/** 获取主办方发布的将来的活动 **/
	public static final String ACCUPASS_FUTUREACTIVITY = ACCUPASS_SERVICE_URL2 + "/org/future_acts";

	/** 获取主办方发布的过去的活动 **/
	public static final String ACCUPASS_OLDACTIVITY = ACCUPASS_SERVICE_URL2 + "/org/old_acts";
	
	/** 主办人信息 **/
	public static final String ACCUPASS_PEOPLE_INFO_DETAILS = ACCUPASS_SERVICE_URL2 + "/user";
	
	/** 关注主办人 **/
	public static final String USER_FOLLOW = ACCUPASS_SERVICE_URL2 + "/user/follow";

	/** 取消关注主办人 **/
	public static final String USER_UNFOLLOW = ACCUPASS_SERVICE_URL2 + "/user/unfollow";
	
	/** 获取主办人活动 **/
	public static final String ACCUPASS_BY_USER_DETAILS = ACCUPASS_SERVICE_URL2 + "/user/acts";
	
	/** 报名活动 **/
	public static final String ACCUPASS_DETAILS_REG = ACCUPASS_SERVICE_URL2 + "/act/reg";
	
	/** 获取常见问题 **/
	public static final String ACCUPASS_GET_FAQ = ACCUPASS_SERVICE_URL2 + "/faq";
	
	/** 抢票活动列表 **/
	public static final String GET_RUSH_EVENTS = ACCUPASS_SERVICE_URL2 + "/act/rush";
	
	/** 加入圈子 **/
	public static final String ACCUPASS_EVENT_CIRCLE = ACCUPASS_SERVICE_URL2 + "/social/attend_circle";
	
	/** 读取群成员信息(包括活跃成员) **/
	public static final String ACCUPASS_GET_CIRCLE_MEMBER = ACCUPASS_SERVICE_URL2 + "/social/circle_member";
	
	/** 获取圈子所有成员 **/
	public static final String social_all_members = ACCUPASS_SERVICE_URL2 + "/social/all_members";
	
	/** 移除成员 **/
	public static final String social_remove_member = ACCUPASS_SERVICE_URL2 + "/social/remove_member";
	
	/** 删除并退出圈子 **/
	public static final String ACCUPASS_QUIT_EVENT_CIRCLE = ACCUPASS_SERVICE_URL2 + "/social/quit_circle";
	
	/** 获取收藏的活动列表 **/
	public static final String ACCUPASS_MYLIKED = ACCUPASS_SERVICE_URL2 + "/act/followed";
	
	/** 获取推荐应用 */
	public static final String RECOMMEND_APP = ACCUPASS_SERVICE_URL2 + "/app_recommend";
	
	/** 获取专题详情 **/
	public static final String NEWS = ACCUPASS_SERVICE_URL2 + "/topic";
	
	/** 获取城市列表 **/
	public static final String GET_HOT_CITYS = ACCUPASS_SERVICE_URL2 + "/hotcitys";
	
	/** 登录 **/
	public static final String ACCUPASS_LOGIN = ACCUPASS_SERVICE_URL3 + "/auth/login";
	/** 获取个人资料 **/
	public static final String ACCUPASS_USER_INFO = ACCUPASS_SERVICE_URL2 + "/user/profile";
	
	/** 获取指定活动所有票券 **/
	public static final String GET_TICKETS = ACCUPASS_SERVICE_URL2 + "/tickets/user_tickets";

	/** 创建私人会话 **/
	public static final String SOCIAL_CONV = ACCUPASS_SERVICE_URL2 + "/social/conv";

	/** 社交 - 获取成员信息 (个人页面) **/
	public static final String social_member = ACCUPASS_SERVICE_URL2 + "/social/member";

	/** 获取用户关注的活动 **/
	public static final String user_follow_acts = ACCUPASS_SERVICE_URL2 + "/user/follow_acts";

	/** 获取主办人发布的活动 **/
	public static final String user_acts = ACCUPASS_SERVICE_URL2 + "/user/acts";

	/** 加入个人黑名单 **/
	public static final String blacklist_add = ACCUPASS_SERVICE_URL2 + "/blacklist/add";

	/** 移除个人黑名单 **/
	public static final String blacklist_remove = ACCUPASS_SERVICE_URL2 + "/blacklist/remove";

	/** 取消关注 **/
	public static final String social_unfollow = ACCUPASS_SERVICE_URL2 + "/social/unfollow";

	/** 申请加好友 **/
	public static final String social_apply = ACCUPASS_SERVICE_URL2 + "/social/apply";

	/** 接受好友邀请 **/
	public static final String social_accept = ACCUPASS_SERVICE_URL2 + "/social/accept";
	
	/** 获取好友列表 **/
	public static final String social_myfollowers = ACCUPASS_SERVICE_URL2 + "/social/myfollowers";
	
	/** 获取好友列表 **/
	public static final String social_report = ACCUPASS_SERVICE_URL2 + "/social/report";
	
	/** 获取公告 **/
	public static final String announcement = ACCUPASS_SERVICE_URL2 + "/announcement";
	
	/** 发布公告**/
	public static final String announcement_publish = ACCUPASS_SERVICE_URL2 + "/announcement/publish";
	
	
	
	
	
	/** 详情验证 **/
	public static final String ACCUPASS_HOME_DETAIL_SECRET = ACCUPASS_SERVICE_URL3 + "/account/secret";

	/** 获取用户标签 **/
	public static final String ACCUPASS_GETUSERTAGS = ACCUPASS_SERVICE_URL2 + "/user/getutags";
	/** 设置用户标签 **/
	public static final String ACCUPASS_SETUSERTAGS = ACCUPASS_SERVICE_URL2 + "/user/setutags";
	/** 取消票券 **/
	public static final String REFUND_TICKET = ACCUPASS_SERVICE_URL2 + "/tickets/refund_ticket";
	
//	/** 票卷详情 **/
//	public static final String GETTICKET = ACCUPASS_SERVICE_URL2 + "/tickets/detail";
	
//	/** 更新百度id **/
//	public static final String ACCUPASS_UPDATEBAIDU = ACCUPASS_SERVICE_URL3 + "/account/updbaiduid";

//	/** 獲取广场 **/
//	public static final String ACCUPASS_SQUAREINFO = ACCUPASS_SERVICE_URL2 + "/square/info";
//	public static final String ACCUPASS_SQUAREADNEWS = ACCUPASS_SERVICE_URL2 + "/square/adnews";
//	public static final String ACCUPASS_SQUAREEVENTS2 = ACCUPASS_SERVICE_URL2 + "/square/events2";

//	/** 獲取首頁資訊 **/
//	public static final String ACCUPASS_HOME = ACCUPASS_SERVICE_URL2 + "/event/home";
	
//	/** 获取热门标签 **/
//	public static final String ACCUPASS_SEARCH_HOTTAG = ACCUPASS_SERVICE_URL2 + "/tag/hot";
	
//	/** 获取主办方发布的活动 **/
//	public static final String ACCUPASS_ORGACT_LIST = ACCUPASS_SERVICE_URL2 + "/org/acts";
	
//	/** 创建活动 **/
//	public static final String ACCUPASS_CREATE_ACCUVALLY = ACCUPASS_SERVICE_URL2 + "/event/create";
	
//	/** 发布活动 **/
//	public static final String ACCUPASS_PULISH_ACCUVALLY = ACCUPASS_SERVICE_URL2 + "/event/pub";
	
//	/** 删除照片 **/
//	public static final String ACCUPASS_DELETEIMAGE = ACCUPASS_SERVICE_URL2 + "/event/delimages";
//	/** 上传活动相册 **/
//	public static final String ACCUPASS_UPLOADIMAGE_ACCUVALLY = ACCUPASS_SERVICE_URL2 + "/event/uploadimages";
//	/** 活动头像 **/
//	public static final String ACCUPASS_ALOGO_ACCUVALLY = ACCUPASS_SERVICE_URL2 + "/upload/alogo";
//	/** 编辑基本信息 **/
//	public static final String ACCUPASS_UPLOAD_ACCUVALLY = ACCUPASS_SERVICE_URL2 + "/event/upd";
//	/** 获取编辑的活动信� **/
//	public static final String ACCUPASS_EDIT_ACCUVALLY = ACCUPASS_SERVICE_URL2 + "/event/editinfo";
	
//	/** 版本检� **/
//	public static final String ACCUPASS_UPDATE_VERSION = ACCUPASS_SERVICE_URL2 + "/version/android";
	
//	/** 支付成功 **/
//	public static final String PAY_SUCC = ACCUPASS_SERVICE_URL2 + "/payment/alipayreturn";
	
//	/** 添加轻活� **/
//	public static final String ACCUPASS_ADD_ACCU = ACCUPASS_SERVICE_URL2 + "/qing/pubqevent";
//	/** 更新轻活� **/
//	public static final String ACCUPASS_UPDATE_LIGHT = ACCUPASS_SERVICE_URL2 + "/qing/updqevent";
//	/** 已发� **/
//	public static final String ACCUPASS_HADPUBS = ACCUPASS_SERVICE_URL2 + "/user/hadpubs";
//	/** 删除报名� **/
//	public static final String ACCUPASS_DETELE_ATTEND = ACCUPASS_SERVICE_URL2 + "/qing/delatds";
//	/** 轻活动详� **/
//	public static final String ACCUPASS_LIGHT_DETAILS = ACCUPASS_SERVICE_URL2 + "/qing/qing";
//	/** 获取活动名单 **/
//	public static final String ACCUPASS_ATTENDS = ACCUPASS_SERVICE_URL2 + "/qing/atds";
//	/** 获取专题精� **/
//	public static final String NEWSLIST = ACCUPASS_SERVICE_URL2 + "/square/newslist";
	
//	/** 绑定第三方账号 **/
//	public static final String ACCUPASS_THREE_BIND = ACCUPASS_SERVICE_URL2 + "/user/bind";
//
//	/** 取消绑定第三方账号 **/
//	public static final String ACCUPASS_THREE_UNBIND = ACCUPASS_SERVICE_URL2 + "/user/unbind";
	
//	/** 获取激情周� **/
//	public static final String WEEKEND = ACCUPASS_SERVICE_URL2 + "/square/weekend";
	
//	/** 获取免费 **/
//	public static final String FREE = ACCUPASS_SERVICE_URL2 + "/square/free";
//	/** 获取创业 **/
//	public static final String ETPNSHIP = ACCUPASS_SERVICE_URL2 + "/square/etpnship";
	
//	/** 限时优惠 **/
//	public static final String SALE = ACCUPASS_SERVICE_URL2 + "/square/sale";
	
//	/** 我的票卷 **/
//	public static final String MYTICKETS = ACCUPASS_SERVICE_URL2 + "/tickets/my";
	
//	/** 已结束票� **/
//	public static final String MYTICKETS_MINE = ACCUPASS_SERVICE_URL2 + "/tickets/finished";
//	/** 下载票卷 **/
//	public static final String DOWNLOADPDF = ACCUPASS_SERVICE_URL2 + "/tickets/pdf";
	
//	/** 未登录票� **/
//	public static final String TICKETACTS = ACCUPASS_SERVICE_URL2 + "/event/ticketacts";
	
//	/** 轻活动报� **/
//	public static final String ACCUPASS_REGISTERQING = ACCUPASS_SERVICE_URL2 + "/qing/regqing";
//	/** 获取轻活动信� **/
//	public static final String ACCUPASS_QINGINFO = ACCUPASS_SERVICE_URL2 + "/qing/qinginfo";
//	/** 沙龙论坛 **/
//	public static final String ACCUPASS_SALON = ACCUPASS_SERVICE_URL2 + "/square/salon";
//	/** 最新活� **/
//	public static final String ACCUPASS_NEW = ACCUPASS_SERVICE_URL2 + "/square/latest";
	
	
//	/** 获取主办� **/
//	public static final String ACCUPASS_ORGANIZER = ACCUPASS_SERVICE_URL2 + "/org/home";
//
//	/** 根据Tag查询主办� **/
//	public static final String ACCUPASS_ORGANIZER_TAG = ACCUPASS_SERVICE_URL2 + "/org/orgs";
	
//	/** 有可能喜欢 **/
//	public static final String PAY_SUCCESS_INTEREST = ACCUPASS_SERVICE_URL2 + "/user/interest";
	
//	/** 获取评论列表 **/
//	public static final String ACCUPASS_COMMENT_LIST = ACCUPASS_SERVICE_URL2 + "/comment/cms";
//	/** 发表评论 **/
//	public static final String ACCUPASS_UPALOD_COMMENT = ACCUPASS_SERVICE_URL2 + "/comment/pub";
}
