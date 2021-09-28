package com.bokeproject.service.impl;

import com.bokeproject.dao.BlogDOMapper;
import com.bokeproject.dao.CommentDOMapper;
import com.bokeproject.dao.UserDOMapper;
import com.bokeproject.dataobject.BlogDO;
import com.bokeproject.dataobject.CommentDO;
import com.bokeproject.dataobject.UserDO;
import com.bokeproject.error.BusinessException;
import com.bokeproject.error.EmBusinessError;
import com.bokeproject.service.BlogService;
import com.bokeproject.service.impl.model.BlogModel;
import com.bokeproject.validator.ValidationResult;
import com.bokeproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private BlogDOMapper blogDOMapper;

    @Autowired
    private CommentDOMapper commentDOMapper;

    @Autowired
    private UserDOMapper userDOMapper;

    private BlogDO convertBlogDOFromBlogModel(BlogModel blogModel){
        if(blogModel==null){
            return null;
        }
        BlogDO blogDO = new BlogDO();
        BeanUtils.copyProperties(blogModel,blogDO);
        return blogDO;

    }
//    private CommentDO convertCommentDOFromBlogModel(BlogModel blogModel){
//        if(blogModel==null){
//            return null;
//        }
//        CommentDO commentDO=new CommentDO();
//        commentDO.setBlogid(blogModel.getBlogid());
//        commentDO.setComments(blogModel.getComments());
//        return commentDO;
//    }


    @Override
    @Transactional
    public BlogModel createBlog(BlogModel blogModel) throws BusinessException {
        //校验入参
        ValidationResult result = validator.validate(blogModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }

        //转化blogmodel->dataobject
        BlogDO blogDO=this.convertBlogDOFromBlogModel(blogModel);

        //写入数据库
        blogDOMapper.insertSelective(blogDO);
        blogModel.setBlogid(blogDO.getBlogid());


//
//        CommentDO commentDO = this.convertCommentDOFromBlogModel(blogModel);
//        commentDOMapper.insertSelective(commentDO);

        //返回创建完成的对象
        return this.getBlogByBlogid(blogModel.getBlogid());
    }

    @Override
    public List<BlogModel> listBlog() {
        List<BlogDO>blogDOList=blogDOMapper.listblog();
//        System.out.println("ServiceImpl层blogDOListSize="+blogDOList.size());
        List<BlogModel>blogModelList=blogDOList.stream().map(blogDO -> {
            UserDO userDO=userDOMapper.selectByPrimaryKey(blogDO.getId());
//            System.out.println("ServiceImpl层userid="+userDO.getId());
            List<CommentDO>commentDOList=commentDOMapper.selectByBlogid(blogDO.getBlogid());
//            System.out.println("ServiceImpl层commentSize="+commentDOList.size());
//            System.out.println("ServiceImpl层blogid="+blogDO.getBlogid());
            BlogModel blogModel=this.convertModelFromDataObject(blogDO,commentDOList,userDO);
            return blogModel;
        }).collect(Collectors.toList());
//        System.out.println("ServiceImpl层blogModelListSize="+blogModelList.size());
        return blogModelList;
    }

    @Override
    public List<BlogModel> listUserBlog(Integer id) {
        List<BlogDO>blogDOList=blogDOMapper.listuserblog(id);
        List<BlogModel>blogModelList=blogDOList.stream().map(blogDO -> {
            UserDO userDO=userDOMapper.selectByPrimaryKey(blogDO.getId());
            List<CommentDO>commentDOList=commentDOMapper.selectByBlogid(blogDO.getBlogid());
            BlogModel blogModel=this.convertModelFromDataObject(blogDO,commentDOList,userDO);
            return blogModel;
        }).collect(Collectors.toList());
        return blogModelList;
    }

    @Override
    public List<BlogModel> listUserSave(List<Integer> BlogIdList) {
        List<BlogDO>blogDOList=blogDOMapper.listUserSave(BlogIdList);
        List<BlogModel>blogModelList=blogDOList.stream().map(blogDO -> {
            UserDO userDO=userDOMapper.selectByPrimaryKey(blogDO.getId());
            List<CommentDO>commentDOList=commentDOMapper.selectByBlogid(blogDO.getBlogid());
            BlogModel blogModel=this.convertModelFromDataObject(blogDO,commentDOList,userDO);
            return blogModel;
        }).collect(Collectors.toList());
        return blogModelList;
    }

    @Override
    public List<BlogModel> listUserZan(List<Integer> blogIdList) {
        List<BlogDO>blogDOList=blogDOMapper.listUserZan(blogIdList);
        List<BlogModel>blogModelList=blogDOList.stream().map(blogDO -> {
            UserDO userDO=userDOMapper.selectByPrimaryKey(blogDO.getId());
            List<CommentDO>commentDOList=commentDOMapper.selectByBlogid(blogDO.getBlogid());
            BlogModel blogModel=this.convertModelFromDataObject(blogDO,commentDOList,userDO);
            return blogModel;
        }).collect(Collectors.toList());
        return blogModelList;
    }



    @Override
    public BlogModel zanBlog(BlogModel blogModel) throws BusinessException {
        //转化blogmodel->dataobject
        BlogDO blogDO=this.convertBlogDOFromBlogModel(blogModel);
        //写入数据库
        blogDOMapper.updateByPrimaryKeySelective(blogDO);
        blogModel.setBlogid(blogDO.getBlogid());
        //返回创建完成的对象
        return this.getBlogByBlogid(blogModel.getBlogid());
    }

    @Override
    public BlogModel clickBlog(BlogModel blogModel) throws BusinessException {
        //转化blogmodel->dataobject
        BlogDO blogDO=this.convertBlogDOFromBlogModel(blogModel);
        //写入数据库
        blogDOMapper.updateByPrimaryKeySelective(blogDO);
        blogModel.setBlogid(blogDO.getBlogid());
        //返回创建完成的对象
        return this.getBlogByBlogid(blogModel.getBlogid());
    }

    @Override
    public List<BlogModel> searchBlog(String shuru) {
        List<BlogDO> searchDOList=blogDOMapper.searchBlog(shuru);
        List<BlogModel>blogModelList=searchDOList.stream().map(blogDO -> {
            UserDO userDO=userDOMapper.selectByPrimaryKey(blogDO.getId());
            List<CommentDO>commentDOList=commentDOMapper.selectByBlogid(blogDO.getBlogid());
            BlogModel blogModel=this.convertModelFromDataObject(blogDO,commentDOList,userDO);
            return blogModel;
        }).collect(Collectors.toList());
        return blogModelList;
    }

    @Override
    public BlogModel getBlogByBlogid(Integer blogid) {
        BlogDO blogDO=blogDOMapper.selectByPrimaryKey(blogid);
        if(blogDO==null){
            return null;
        }
        //操作获得评论
        List<CommentDO>commentDOList=commentDOMapper.selectByBlogid(blogDO.getBlogid());
        UserDO userDO=userDOMapper.selectByPrimaryKey(blogDO.getId());
//        //将dataobject转化成model
        BlogModel blogModel=convertModelFromDataObject(blogDO,commentDOList,userDO);

        return blogModel;
    }

    @Override
    public Boolean deleteBlog(Integer blogid) {
        int isSuccess=blogDOMapper.deleteByPrimaryKey(blogid);
        if(isSuccess==1){
            return true;
        }
        return false;
    }

    @Override
    public Boolean xiugai(BlogModel blogModel) {

        BlogDO blogDO=convertBlogDOFromBlogModel(blogModel);
        int isSuccess=blogDOMapper.updateByPrimaryKey(blogDO);
        if(isSuccess==1){
            return true;
        }
        return false;
    }


    private  BlogModel convertModelFromDataObject(BlogDO blogDO,List<CommentDO> commentDOList,UserDO userDO){
        BlogModel blogModel=new BlogModel();
        BeanUtils.copyProperties(blogDO,blogModel);
        blogModel.setCommentnum(commentDOList.size());
        blogModel.setUser(userDO.getName());
        return blogModel;
    }
}
