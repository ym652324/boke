package com.bokeproject.service.impl;


import com.bokeproject.dao.MessageDOMapper;
import com.bokeproject.dao.UserDOMapper;
import com.bokeproject.dataobject.CommentDO;
import com.bokeproject.dataobject.MessageDO;
import com.bokeproject.dataobject.UserDO;
import com.bokeproject.error.BusinessException;
import com.bokeproject.error.EmBusinessError;
import com.bokeproject.service.MessageService;
import com.bokeproject.service.impl.model.MessageModel;
import com.bokeproject.validator.ValidationResult;
import com.bokeproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private MessageDOMapper messageDOMapper;

    @Autowired
    private UserDOMapper userDOMapper;

    private MessageDO convertMessageDOFromMessageModel(MessageModel messageModel){
        if(messageModel==null){
            return null;
        }
        MessageDO messageDO = new MessageDO();
        BeanUtils.copyProperties(messageModel,messageDO);
        return messageDO;

    }

    @Override
    @Transactional
    public MessageModel createMessage(MessageModel messageModel) throws BusinessException {
        ValidationResult result = validator.validate(messageModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //转化blogmodel->dataobject
        MessageDO messageDO=this.convertMessageDOFromMessageModel(messageModel);

        //写入数据库
        messageDOMapper.insertSelective(messageDO);
//        System.out.println("写入数据库后messageid:"+messageDO.getMessageid());
        messageModel.setMessageid(messageDO.getMessageid());


        //返回创建完成的对象
        return messageModel;
    }

    @Override
    public List<MessageModel> listMessage() {
        List<MessageDO>messageDOList= messageDOMapper.listMessage();
        System.out.println("messageDOListSize:"+messageDOList.size());
//        if(messageDOList==null||messageDOList.size()==0){
//            return null;
//        }
        List<MessageModel>messageModelList=messageDOList.stream().map(messageDO -> {
            UserDO userDO=userDOMapper.selectByPrimaryKey(messageDO.getId());
            System.out.println("ServiceImpl层userid="+userDO.getId());
            MessageModel messageModel=this.convertModelFromDataObject(messageDO,userDO);
            System.out.println("ServiceImpl层messageid="+messageModel.getMessageid());
            return messageModel;
        }).collect(Collectors.toList());
        System.out.println("messageModelList.size="+messageModelList.size());
        return messageModelList;
    }

    @Override
    public List<MessageModel> listUserMessage(Integer id) {
        List<MessageDO>messageDOList= messageDOMapper.listUserMessage(id);
        System.out.println("messageDOListSize:"+messageDOList.size());
        if(messageDOList==null){
            return null;
        }
        List<MessageModel>messageModelList=messageDOList.stream().map(messageDO -> {
            UserDO userDO=userDOMapper.selectByPrimaryKey(messageDO.getId());
                    System.out.println("ServiceImpl层userid="+userDO.getId());
            MessageModel messageModel=this.convertModelFromDataObject(messageDO,userDO);

            return messageModel;
        }).collect(Collectors.toList());
        System.out.println(messageModelList.size());
        return messageModelList;
    }

    @Override
    public MessageModel getMessage(Integer messageid) {
        MessageDO messageDO=messageDOMapper.selectByPrimaryKey(messageid);
        MessageModel messageModel=new MessageModel();
        BeanUtils.copyProperties(messageDO,messageModel);
        return messageModel;
    }

    private MessageModel convertModelFromDataObject(MessageDO messageDO,UserDO userDO){
        MessageModel messageModel=new MessageModel();
        BeanUtils.copyProperties(messageDO,messageModel);
        messageModel.setUser(userDO.getName());
        return messageModel;
    }
}
