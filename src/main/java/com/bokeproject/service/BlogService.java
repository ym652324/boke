package com.bokeproject.service;

import com.bokeproject.error.BusinessException;
import com.bokeproject.service.impl.model.BlogModel;

import java.util.List;

public interface BlogService {
    //创建博客
    BlogModel createBlog(BlogModel blogModel) throws BusinessException;

    //博客列表浏览
    List<BlogModel> listBlog();
    List<BlogModel> listUserBlog(Integer id);
    List<BlogModel> listUserSave(List<Integer> BlogIdList);
    List<BlogModel> listUserZan(List<Integer> blogIdList);

    //博客点赞
    BlogModel zanBlog(BlogModel blogModel)throws BusinessException;
    //浏览量统计
    BlogModel clickBlog(BlogModel blogModel)throws BusinessException;
    //标题搜索
    List<BlogModel> searchBlog(String shuru);

    //博客详情浏览
    BlogModel getBlogByBlogid(Integer blogid);
    Boolean deleteBlog(Integer blogid);
    Boolean xiugai(BlogModel blogModel);



}
