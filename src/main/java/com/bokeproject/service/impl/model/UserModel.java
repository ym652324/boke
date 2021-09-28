package com.bokeproject.service.impl.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserModel {
    private Integer id;
    @NotBlank(message="用户名不能为空")
    private String name;
    @NotNull(message="性别不能不填写")
    private Byte gender;
    @NotNull(message="年龄不能不填写")
    private Integer age;
    @NotBlank(message="手机号不能为空")
    private String telphone;
    private String iconurl;
    private Byte isadmin;
    @NotBlank(message="密码不能为空")
    private String password;

    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

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
    public void setGender( Byte gender) {
        this.gender = gender;
    }

    public String getTelphone() {
        return telphone;
    }
    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getIconurl() { return iconurl; }
    public void setIconurl(String iconurl) { this.iconurl = iconurl; }

    public Byte getIsadmin() { return isadmin; }
    public void setIsadmin(Byte isadmin) { this.isadmin = isadmin;}

}
