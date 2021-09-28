package com.bokeproject.service.impl;

import com.bokeproject.dao.UserDOMapper;
import com.bokeproject.dataobject.UserDO;
import com.bokeproject.error.BusinessException;
import com.bokeproject.error.EmBusinessError;
import com.bokeproject.service.UserService;
import com.bokeproject.service.impl.model.UserModel;
import com.bokeproject.validator.ValidationResult;
import com.bokeproject.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDOMapper userDOMapper;

    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id){
        //调用userdomapper获取到对应的用户dataobject
        UserDO userDO=userDOMapper.selectByPrimaryKey(id);
        if(userDO==null){
            return null;
        }
        return convertFromDataObject(userDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        if(userModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        ValidationResult result=validator.validate(userModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //实现model->dataobject方法
        UserDO userDO=convertFromModel(userModel);
        userDOMapper.insertSelective(userDO);


        return;

    }
//
//    @Override
//    public void addUser(UserModel userModel) throws BusinessException {
//        if(userModel==null){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
//        ValidationResult result=validator.validate(userModel);
//        if(result.isHasErrors()){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
//        }
//
//        //实现model->dataobject方法
//        UserDO userDO=convertFromModel(userModel);
//        userDOMapper.insertSelective(userDO);
//        return;
//    }

    @Override
    public UserModel validateLogin(String telphone, String password) throws BusinessException {
        //通过用户的手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        UserModel userModel = convertFromDataObject(userDO);
        //比对用户信息内加密的密码是否和传输进来的密码相匹配
        if(!StringUtils.equals(password,userModel.getPassword())){
            throw  new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    @Override
    public List<UserModel>listUser(Integer isAdmin){
        List<UserDO> userDOList=userDOMapper.listUser(isAdmin);
        List<UserModel> userModelList = userDOList.stream().map(userDO -> {
            UserModel userModel=this.convertFromDataObject(userDO);
            return userModel;
        }).collect(Collectors.toList());
        return userModelList;
    }

    @Override
    public List<UserModel> pagechange(Integer start,Integer isAdmin) {
        List<UserDO> userDOList=userDOMapper.pagechange(start,isAdmin);
        List<UserModel> userModelList = userDOList.stream().map(userDO -> {
            UserModel userModel=this.convertFromDataObject(userDO);
            return userModel;
        }).collect(Collectors.toList());
        return userModelList;
    }

    @Override
    public UserModel selectByTelphone(String telphone) {
        UserDO userDO=userDOMapper.selectByTelphone(telphone);
        if(userDO==null){
            return null;
        }
        return convertFromDataObject(userDO);
    }

    @Override
    public List<UserModel> selectByName(String name) {
        List<UserDO> userDOList=userDOMapper.selectByName(name);
        List<UserModel> userModelList = userDOList.stream().map(userDO -> {
            UserModel userModel=this.convertFromDataObject(userDO);
            return userModel;
        }).collect(Collectors.toList());
        return userModelList;
    }

    @Override
    public boolean deleteUserById(Integer[] arr){
        int isSuccess=userDOMapper.deleteUserById(arr);
        if(isSuccess==arr.length){
            return true;
        }
        return false;
    }

    @Override
    public boolean toBeAdmin(Integer[] arr) {
        int isSuccess=userDOMapper.toBeAdmin(arr);
        System.out.println(isSuccess);
        if(isSuccess==arr.length){
            return true;
        }
        return false;
    }


    private  UserDO convertFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserDO userDO=new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }
    private UserModel convertFromDataObject(UserDO userDO){
        if(userDO==null){
            return null;
        }
        UserModel userModel=new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        return userModel;
    }





}
