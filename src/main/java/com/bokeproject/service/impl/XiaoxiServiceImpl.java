package com.bokeproject.service.impl;

import com.bokeproject.dao.*;
import com.bokeproject.dataobject.*;
import com.bokeproject.error.BusinessException;
import com.bokeproject.error.EmBusinessError;
import com.bokeproject.service.XiaoxiService;
import com.bokeproject.service.impl.model.MessageModel;
import com.bokeproject.service.impl.model.XiaoxiModel;
import com.bokeproject.validator.ValidationResult;
import com.bokeproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class XiaoxiServiceImpl implements XiaoxiService {
    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private XiaoxiDOMapper xiaoxiDOMapper;

    @Autowired
    private MessageDOMapper messageDOMapper;

    @Autowired
    private CommentDOMapper commentDOMapper;

    @Autowired
    private BlogDOMapper blogDOMapper;

    @Autowired
    private UserDOMapper userDOMapper;
    @Override
    @Transactional
    public XiaoxiModel createXiaoxi(XiaoxiModel xiaoxiModel) throws BusinessException {
        ValidationResult result = validator.validate(xiaoxiModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //转化blogmodel->dataobject
        XiaoxiDO xiaoxiDO=this.convertXiaoxiDOFromXiaoxiModel(xiaoxiModel);

        //写入数据库
        xiaoxiDOMapper.insertSelective(xiaoxiDO);
        xiaoxiModel.setXiaoxiid(xiaoxiDO.getXiaoxiid());

        //返回创建完成的对象
        return xiaoxiModel;
    }

    @Override
    public List<XiaoxiModel> listXiaoxi(Integer acceptid) {
        List<XiaoxiDO> xiaoxiDOList= xiaoxiDOMapper.listXiaoxi(acceptid);;
        if(null==xiaoxiDOList||xiaoxiDOList.size()==0){
            return null;
        }
        List<XiaoxiModel> xiaoxiModelList =xiaoxiDOList.stream().map(xiaoxiDO -> {
//            UserDO userDO=null;
//            if(com.alibaba.druid.util.StringUtils.equals(xiaoxiDO.getType(),"comment")){
//                BlogDO blogDO=blogDOMapper.selectByPrimaryKey(xiaoxiDO.getBlogormessageid());
//                userDO=userDOMapper.selectByPrimaryKey(blogDO.getId());
//            }
//            else{
//                MessageDO messageDO=messageDOMapper.selectByPrimaryKey(xiaoxiDO.getBlogormessageid());
//                userDO=userDOMapper.selectByPrimaryKey(messageDO.getId());
//            }
            UserDO userDO=userDOMapper.selectByPrimaryKey(xiaoxiDO.getSendid());
            XiaoxiModel xiaoxiModel=this.convertModelFromDataObject(xiaoxiDO,userDO);
            return xiaoxiModel;
        }).collect(Collectors.toList());
        return xiaoxiModelList;
    }

    private XiaoxiModel convertModelFromDataObject(XiaoxiDO xiaoxiDO,UserDO userDO){
        XiaoxiModel xiaoxiModel=new XiaoxiModel();
        BeanUtils.copyProperties(xiaoxiDO,xiaoxiModel);
        xiaoxiModel.setSendman(userDO.getName());
//        xiaoxiModel.setSendid(userDO.getId());

        return xiaoxiModel;
    }

    private XiaoxiDO convertXiaoxiDOFromXiaoxiModel(XiaoxiModel xiaoxiModel){
        if(xiaoxiModel==null){
            return null;
        }
        XiaoxiDO xiaoxiDO = new XiaoxiDO();
        BeanUtils.copyProperties(xiaoxiModel,xiaoxiDO);
        return xiaoxiDO;

    }
}
