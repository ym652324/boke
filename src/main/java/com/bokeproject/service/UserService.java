package com.bokeproject.service;

import com.bokeproject.error.BusinessException;
import com.bokeproject.service.impl.model.UserModel;

import java.util.List;

public interface UserService {
    //通过用户id获取用户对象的方法
    UserModel getUserById(Integer id);


    void register(UserModel userModel) throws BusinessException;
    List<UserModel> listUser(Integer isAdmin);
    List<UserModel> pagechange(Integer start,Integer isAdmin);
    UserModel selectByTelphone(String telphone);
    List<UserModel> selectByName(String name);
    boolean deleteUserById(Integer[] arr);
    boolean toBeAdmin(Integer[] arr);


    /*
    telphone:用户注册手机
    passwordJM：用户加密后的密码
     */
    UserModel validateLogin(String telphone,String password) throws BusinessException;
}
