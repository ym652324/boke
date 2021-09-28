package com.bokeproject.controller.viewobject;

public class XiaoxiVO {
    private Integer xiaoxiid;
    private Integer blogormessageid;
    private  Integer acceptid;
    private String type;
    private String xiaoxi;
    private String xiaoxidate;
    private String sendman;
    private Integer sendid;

    public Integer getSendid() {
        return sendid;
    }

    public void setSendid(Integer sendid) {
        this.sendid = sendid;
    }

    public Integer getBlogormessageid() {
        return blogormessageid;
    }

    public void setBlogormessageid(Integer blogormessageid) {
        this.blogormessageid = blogormessageid;
    }

    public String getSendman() {
        return sendman;
    }

    public void setSendman(String sendman) {
        this.sendman = sendman;
    }



    public Integer getXiaoxiid() {
        return xiaoxiid;
    }

    public void setXiaoxiid(Integer xiaoxiid) {
        this.xiaoxiid = xiaoxiid;
    }

    public String getXiaoxidate() {
        return xiaoxidate;
    }

    public void setXiaoxidate(String xiaoxidate) {
        this.xiaoxidate = xiaoxidate;
    }



    public Integer getAcceptid() {
        return acceptid;
    }

    public void setAcceptid(Integer acceptid) {
        this.acceptid = acceptid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getXiaoxi() {
        return xiaoxi;
    }

    public void setXiaoxi(String xiaoxi) {
        this.xiaoxi = xiaoxi;
    }
}
