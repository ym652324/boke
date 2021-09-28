package com.bokeproject.service.impl;

import com.bokeproject.dao.AnnouncementDOMapper;
import com.bokeproject.dao.UserDOMapper;
import com.bokeproject.dataobject.AnnouncementDO;
import com.bokeproject.dataobject.BlogDO;
import com.bokeproject.dataobject.CommentDO;
import com.bokeproject.dataobject.UserDO;
import com.bokeproject.error.BusinessException;
import com.bokeproject.error.EmBusinessError;
import com.bokeproject.service.AnnounceService;
import com.bokeproject.service.impl.model.AnnounceModel;
import com.bokeproject.service.impl.model.BlogModel;
import com.bokeproject.service.impl.model.UserModel;
import com.bokeproject.validator.ValidationResult;
import com.bokeproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AnnounceServiceImpl implements AnnounceService {
    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private AnnouncementDOMapper announcementDOMapper;

    @Autowired
    private UserDOMapper userDOMapper;


    @Override
    public List<AnnounceModel> listAnnounce() {
        List<AnnouncementDO>announcementDOList=announcementDOMapper.listannounce();
        List<AnnounceModel>announceModelList=announcementDOList.stream().map(announcementDO -> {
            UserDO userDO=userDOMapper.selectByPrimaryKey(announcementDO.getId());
            AnnounceModel announceModel=this.convertModelFromDataObject(announcementDO,userDO);
            return announceModel;
        }).collect(Collectors.toList());
        return announceModelList;
    }

    @Override
    public AnnounceModel clickAnnounce(AnnounceModel announceModel) throws BusinessException {
        AnnouncementDO announcementDO=this.convertAnnounceDOFromAnnounceModel(announceModel);
        //写入数据库
        announcementDOMapper.updateByPrimaryKeySelective(announcementDO);
        announceModel.setAnnounceid(announcementDO.getAnnounceid());
        //返回创建完成的对象
        return this.getAnnounceByAnnounceid(announceModel.getAnnounceid());
    }

    @Override
    public AnnounceModel getAnnounceByAnnounceid(Integer announceid) {
        AnnouncementDO announcementDO=announcementDOMapper.selectByPrimaryKey(announceid);
        UserDO userDO=userDOMapper.selectByPrimaryKey(announcementDO.getId());
        if(announceid==null){
            return null;
        }
//        //将dataobject转化成model
        AnnounceModel announceModel=convertModelFromDataObject(announcementDO,userDO);
        return announceModel;
    }

    @Override
    @Transactional
    public AnnounceModel addAnnounce(AnnounceModel announceModel)throws BusinessException {
        if(announceModel==null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        ValidationResult result=validator.validate(announceModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        //转化blogmodel->dataobject
        AnnouncementDO announcementDO=this.convertAnnounceDOFromAnnounceModel(announceModel);

        //写入数据库
        announcementDOMapper.insertSelective(announcementDO);
        announceModel.setAnnounceid(announcementDO.getAnnounceid());


        return this.getAnnounceByAnnounceid(announceModel.getAnnounceid());
    }

    @Override
    public List<AnnounceModel> searchAnnounce(String shuru) {
        List<AnnouncementDO> searchDOList=announcementDOMapper.searchAnnounceByTitle(shuru);
        List<AnnounceModel>announceModelList=searchDOList.stream().map(announcementDO -> {
            UserDO userDO=userDOMapper.selectByPrimaryKey(announcementDO.getId());
            AnnounceModel announceModel=this.convertModelFromDataObject(announcementDO,userDO);
            return announceModel;
        }).collect(Collectors.toList());
        return announceModelList;

    }

    @Override
    public boolean deleteAnnounceById(Integer[] arr) {
        int isSuccess=announcementDOMapper.deleteAnnounceById(arr);
        System.out.println(isSuccess);
        if(isSuccess==arr.length){
            return true;
        }
        return false;
    }

    @Override
    public List<AnnounceModel> pagechange(Integer start) {
        List<AnnouncementDO> announcementDOList=announcementDOMapper.pagechange(start);
        List<AnnounceModel> announceModelList = announcementDOList.stream().map(announcementDO -> {
            UserDO userDO=userDOMapper.selectByPrimaryKey(announcementDO.getId());
            AnnounceModel announceModel=this.convertModelFromDataObject(announcementDO,userDO);
            return announceModel;
        }).collect(Collectors.toList());
        return announceModelList;
    }


    private  AnnounceModel convertModelFromDataObject(AnnouncementDO announcementDO,UserDO userDO){
        AnnounceModel announceModel=new AnnounceModel();
        BeanUtils.copyProperties(announcementDO,announceModel);
        announceModel.setAdmin(userDO.getName());
        return announceModel;
    }

    private AnnouncementDO convertAnnounceDOFromAnnounceModel(AnnounceModel announceModel){
        if(announceModel==null){
            return null;
        }
        AnnouncementDO announcementDO = new AnnouncementDO();
        BeanUtils.copyProperties(announceModel,announcementDO);
        return announcementDO;

    }
}
