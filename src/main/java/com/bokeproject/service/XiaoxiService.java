package com.bokeproject.service;

import com.bokeproject.error.BusinessException;
import com.bokeproject.service.impl.model.XiaoxiModel;

import java.util.List;

public interface XiaoxiService {
    XiaoxiModel createXiaoxi(XiaoxiModel xiaoxiModel) throws BusinessException;
    List<XiaoxiModel> listXiaoxi(Integer acceptid);
}
