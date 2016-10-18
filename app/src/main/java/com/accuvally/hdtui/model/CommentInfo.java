package com.accuvally.hdtui.model;

import java.io.Serializable;

//获取评论
public class CommentInfo implements Serializable {
	
	private String Id;

	private String Rid;

	private String Content;

	private String CreateDate;

	private String Logo;

	private String Nick;

    private int ReplyType;//回复类型（1=活动评价，2=活动咨询）


    public String getImgs() {
        return Imgs;
    }

    public void setImgs(String imgs) {
        Imgs = imgs;
    }

    private String Imgs;//图片CDN
//    private List<String> Imgs;//图片CDN


    public int getReplyType() {
        return ReplyType;
    }

    public void setReplyType(int replyType) {
        ReplyType = replyType;
    }



	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getRid() {
		return Rid;
	}

	public void setRid(String rid) {
		Rid = rid;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(String createDate) {
		CreateDate = createDate;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public String getNick() {
		return Nick;
	}

	public void setNick(String nick) {
		Nick = nick;
	}

}
