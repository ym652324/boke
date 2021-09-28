package com.bokeproject.service;

import com.bokeproject.service.impl.model.ZanModel;

import java.util.List;

public interface ZanService {
    List<Integer> listUserZanBlog(Integer id);
    void zanBlog(ZanModel zanModel);
    void cancelZanBlog(Integer blogid,Integer id);
    Boolean selectByIdandBlogId(Integer id,Integer blogid);
}
