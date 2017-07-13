package com.accuvally.hdtui.model;

import com.accuvally.hdtui.utils.TimeUtils;

import java.io.Serializable;

/**
 * Created by Andy Liu on 2017/7/9.
 */
public class PublishBean implements Serializable {


    public String id;
    public String title;
    public String logo;
    public String startutc;
    public String endutc;
    public int status;
    public String statusstr;
    public int regnum;//购票数
    public String address;

    public String timestr;//评价展示的时间
    public String orgname;


    /** 活动开始本地时间 **/
    public String getStartutc() {
        return TimeUtils.utcToLocal(startutc);
    }

}
