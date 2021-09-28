package com.bokeproject.service.impl;

import com.bokeproject.controller.viewobject.BlogVO;
import com.bokeproject.dao.CommentDOMapper;
import com.bokeproject.dao.UserDOMapper;
import com.bokeproject.dataobject.BlogDO;
import com.bokeproject.dataobject.CommentDO;
import com.bokeproject.dataobject.UserDO;
import com.bokeproject.error.BusinessException;
import com.bokeproject.error.EmBusinessError;
import com.bokeproject.service.CommentService;
import com.bokeproject.service.impl.model.BlogModel;
import com.bokeproject.service.impl.model.CommentModel;
import com.bokeproject.validator.ValidationResult;
import com.bokeproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private CommentDOMapper commentDOMapper;

    @Autowired
    private UserDOMapper userDOMapper;

    private CommentDO convertCommentDOFromCommentModel(CommentModel commentModel){
        if(commentModel==null){
            return null;
        }
        CommentDO commentDO = new CommentDO();
        BeanUtils.copyProperties(commentModel,commentDO);
        return commentDO;

    }


    @Override
    @Transactional
    public CommentModel createComment(CommentModel commentModel) throws BusinessException {
        //校验入参
        ValidationResult result = validator.validate(commentModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //转化blogmodel->dataobject
        CommentDO commentDO=this.convertCommentDOFromCommentModel(commentModel);

        //写入数据库
        commentDOMapper.insertSelective(commentDO);
        commentModel.setCommentid(commentDO.getCommentid());

        //返回创建完成的对象
        return commentModel;
    }

    @Override
    public List<CommentModel> getCommentByBlogid(Integer blogid) {
        List<CommentDO>commentDOList = commentDOMapper.selectByBlogid(blogid);
        if(commentDOList==null){
            return null;
        }
        List<CommentModel>commentModelList=commentDOList.stream().map(commentDO -> {
            UserDO userDO=userDOMapper.selectByPrimaryKey(commentDO.getId());
            CommentModel commentModel=this.convertModelFromDataObject(commentDO,userDO);
            return commentModel;
        }).collect(Collectors.toList());
        return commentModelList;

    }

    private CommentModel convertModelFromDataObject(CommentDO commentDO,UserDO userDO){
        CommentModel commentModel=new CommentModel();
        BeanUtils.copyProperties(commentDO,commentModel);
        commentModel.setUser(userDO.getName());
        return commentModel;
    }


}