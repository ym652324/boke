package com.bokeproject.controller.viewobject;

public class UserVO {
    private Integer id;
    private String name;
    private Integer age;
    private Byte gender;
    private String telphone;
    private String iconurl;
    private Byte isadmin;

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconUrl) {
        this.iconurl = iconUrl;
    }

    public Byte getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(Byte isadmin) {
        this.isadmin = isadmin;
    }

    public Integer getAge() {  return age;}
    public void setAge(Integer age) { this.age = age;}

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }
    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getTelphone() {
        return telphone;
    }
    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }
}
