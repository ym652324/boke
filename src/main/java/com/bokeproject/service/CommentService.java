package com.bokeproject.service;

import com.bokeproject.error.BusinessException;
import com.bokeproject.service.impl.model.CommentModel;

import java.util.List;

public interface CommentService {
    //创建评价
    CommentModel createComment(CommentModel commentModel) throws BusinessException;
    //通过blog id搜索评价
    List<CommentModel> getCommentByBlogid(Integer blogid);
}
