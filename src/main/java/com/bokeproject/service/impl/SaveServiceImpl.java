package com.bokeproject.service.impl;

import com.bokeproject.dao.SaveDOMapper;
import com.bokeproject.dataobject.SaveDO;
import com.bokeproject.error.BusinessException;
import com.bokeproject.error.EmBusinessError;
import com.bokeproject.service.SaveService;
import com.bokeproject.service.impl.model.SaveModel;
import com.bokeproject.validator.ValidationResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SaveServiceImpl implements SaveService {
    @Autowired
    SaveDOMapper saveDOMapper;
    @Override
    public List<Integer> listUserSaveBlog(Integer id) {
        List<Integer> blogIds=saveDOMapper.listUserSaveBlog(id);
        if(blogIds.size()==0||blogIds==null){
            return null;
        }
        return blogIds;
    }

    @Override
    public void saveBlog(SaveModel saveModel) {

        SaveDO saveDO=convertFromModel(saveModel);
        saveDOMapper.insertSelective(saveDO);
    }

    @Override
    public void cancelSaveBlog(Integer blogid, Integer id) {
        saveDOMapper.deleteSave(blogid,id);
    }

    @Override
    public Boolean selectByIdandBlogId(Integer id, Integer blogid) {
        if(saveDOMapper.selectByIdandBlogId(id,blogid)==null){
            return false;
        }
        return true;
    }

    private  SaveDO convertFromModel(SaveModel saveModel){
        if(saveModel==null){
            return null;
        }
        SaveDO saveDO=new SaveDO();
        BeanUtils.copyProperties(saveModel,saveDO);
        return saveDO;
    }
}
