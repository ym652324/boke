package com.bokeproject.service.impl.model;

import javax.validation.constraints.NotBlank;

public class AnnounceModel {
    public Integer announceid;
    public Integer id;

    @NotBlank(message="公告内容不能为空")
    public String announcements;

    public String announcedate;

    public Integer announceclick;

    @NotBlank(message="公告标题不能为空")
    public String announcetitle;

    public String admin;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public Integer getAnnounceid() {
        return announceid;
    }

    public void setAnnounceid(Integer announceid) {
        this.announceid = announceid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(String announcements) {
        this.announcements = announcements;
    }

    public String getAnnouncedate() {
        return announcedate;
    }

    public void setAnnouncedate(String announcedate) {
        this.announcedate = announcedate;
    }

    public Integer getAnnounceclick() {
        return announceclick;
    }

    public void setAnnounceclick(Integer announceclick) {
        this.announceclick = announceclick;
    }

    public String getAnnouncetitle() {
        return announcetitle;
    }

    public void setAnnouncetitle(String announcetitle) {
        this.announcetitle = announcetitle;
    }
}
