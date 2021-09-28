package com.bokeproject.service;

import com.bokeproject.error.BusinessException;
import com.bokeproject.service.impl.model.MessageModel;

import java.util.List;

public interface MessageService {
    MessageModel createMessage(MessageModel messageModel) throws BusinessException;
    List<MessageModel> listMessage();
    List<MessageModel> listUserMessage(Integer id);
    MessageModel getMessage(Integer messageid);

}
