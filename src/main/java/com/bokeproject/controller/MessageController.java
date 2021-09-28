package com.bokeproject.controller;


import com.bokeproject.controller.viewobject.MessageVO;
import com.bokeproject.error.BusinessException;
import com.bokeproject.response.CommonReturnType;
import com.bokeproject.service.MessageService;
import com.bokeproject.service.impl.model.CommentModel;
import com.bokeproject.service.impl.model.MessageModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller("message")
@RequestMapping("/message")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class MessageController extends BaseController{
    @Autowired
    MessageService messageService;

    @RequestMapping(value = "/create",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType createMessage(@RequestParam(name="messages")String messages,
                                          @RequestParam(name="id")Integer id,
                                          @RequestParam(name="fathermessageid")Integer fathermessageid,
                                          @RequestParam(name="messagepersonid")Integer messagepersonid

    ) throws BusinessException {
        //封装Service请求用来创建博客

        MessageModel messageModel=new MessageModel();
        messageModel.setMessages(messages);
        Date date = new Date();
        //设置要获取到什么样的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取String类型的时间
        String createdate = sdf.format(date);
        messageModel.setMessagedate(createdate);
        messageModel.setMessagepersonid(messagepersonid);
        messageModel.setId(id);
        messageModel.setFathermessageid(fathermessageid);


        MessageModel messageModelForReturn=messageService.createMessage(messageModel);
        MessageVO messageVO=convertFromModel(messageModelForReturn);
//        System.out.println("messageid:"+messageVO.getMessageid());
//        MessageModel messageModel1=messageService.getMessage(messageVO.getMessageid());
//        MessageVO messageVO1=convertFromModel(messageModel1);
        return CommonReturnType.create(messageVO);
    }

    private MessageVO convertFromModel(MessageModel messageModel){
        if(messageModel==null){
            return null;
        }
        MessageVO messageVO = new MessageVO();
        BeanUtils.copyProperties(messageModel,messageVO);
        return messageVO;
    }

    @RequestMapping(value = "/listmessage",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType listMessage() {
        List<MessageModel> messageModelList=messageService.listMessage();
        //使用stream api将list内的blogModel转化为BlogVO

        List<MessageVO> messageVOList=messageModelList.stream().map(messageModel -> {

            MessageVO messageVO=this.convertFromModel(messageModel);
            return messageVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(messageVOList);
    }
    @RequestMapping(value = "/listUserMessage",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType listUserMessage(@RequestParam(name="id")Integer id) {
        List<MessageModel> messageModelList=messageService.listUserMessage(id);
        //使用stream api将list内的blogModel转化为BlogVO
        if(messageModelList==null){
            return CommonReturnType.create(null);
        }
        List<MessageVO> messageVOList=messageModelList.stream().map(messageModel -> {

            MessageVO messageVO=this.convertFromModel(messageModel);
            return messageVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(messageVOList);
    }
}
