package com.bokeproject.service;

import com.bokeproject.service.impl.model.SaveModel;

import java.util.List;

public interface SaveService {
    List<Integer> listUserSaveBlog(Integer id);
    void saveBlog(SaveModel saveModel);
    void cancelSaveBlog(Integer blogid,Integer id);
    Boolean selectByIdandBlogId(Integer id,Integer blogid);
}
