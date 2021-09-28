package com.bokeproject.service.impl;

import com.bokeproject.dao.ZanDOMapper;
import com.bokeproject.dataobject.SaveDO;
import com.bokeproject.dataobject.ZanDO;
import com.bokeproject.service.ZanService;
import com.bokeproject.service.impl.model.ZanModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZanServiceImpl implements ZanService {
    @Autowired
    ZanDOMapper zanDOMapper;

    @Override
    public List<Integer> listUserZanBlog(Integer id) {
        List<Integer> blogIds=zanDOMapper.listUserZanBlog(id);
        if(blogIds.size()==0||blogIds==null){
            return null;
        }
        return blogIds;
    }

    @Override
    public void zanBlog(ZanModel zanModel) {
        ZanDO zanDO=convertFromModel(zanModel);
        zanDOMapper.insertSelective(zanDO);
    }

    @Override
    public void cancelZanBlog(Integer blogid, Integer id) {
        zanDOMapper.deleteZan(blogid,id);
    }

    @Override
    public Boolean selectByIdandBlogId(Integer id, Integer blogid) {
        if(zanDOMapper.selectByIdandBlogId(id,blogid)==null){
            return false;
        }
        return true;
    }

    private ZanDO convertFromModel(ZanModel zanModel){
        if(zanModel==null){
            return null;
        }
        ZanDO zanDO=new ZanDO();
        BeanUtils.copyProperties(zanModel,zanDO);
        return zanDO;
    }
}
