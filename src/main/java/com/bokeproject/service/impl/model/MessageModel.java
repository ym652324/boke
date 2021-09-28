package com.bokeproject.service.impl.model;

import javax.validation.constraints.NotBlank;

public class MessageModel {
    private Integer messageid;
    @NotBlank(message = "留言不能为空")
    private String messages;
    private Integer id;
    private Integer messagepersonid;
    private String messagedate;
    private Integer fathermessageid;
    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public Integer getMessageid() {
        return messageid;
    }

    public void setMessageid(Integer messageid) {
        this.messageid = messageid;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMessagepersonid() {
        return messagepersonid;
    }

    public void setMessagepersonid(Integer messagepersonid) {
        this.messagepersonid = messagepersonid;
    }

    public String getMessagedate() {
        return messagedate;
    }

    public void setMessagedate(String messagedate) {
        this.messagedate = messagedate;
    }

    public Integer getFathermessageid() {
        return fathermessageid;
    }

    public void setFathermessageid(Integer fathermessageid) {
        this.fathermessageid = fathermessageid;
    }
}
