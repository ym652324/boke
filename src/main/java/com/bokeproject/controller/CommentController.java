package com.bokeproject.controller;


import com.bokeproject.controller.viewobject.CommentVO;
import com.bokeproject.error.BusinessException;
import com.bokeproject.response.CommonReturnType;
import com.bokeproject.service.CommentService;
import com.bokeproject.service.impl.model.BlogModel;
import com.bokeproject.service.impl.model.CommentModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller("comment")
@RequestMapping("/comment")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class CommentController extends BaseController{
    @Autowired
    private CommentService commentService;
    @RequestMapping(value = "/create",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType createComment(@RequestParam(name="comments")String comments,
                                       @RequestParam(name="blogid")Integer blogid,
                                       @RequestParam(name="id")Integer id,
                                          @RequestParam(name="fathercommentid")Integer fathercommentid
//                                          @RequestParam(name="commentaterid")Integer commentaterid
//                                       @RequestParam(name="fathercommentid")Integer fathercommentid


    ) throws BusinessException {
        //封装Service请求用来创建博客

        CommentModel commentModel=new CommentModel();
        commentModel.setComments(comments);
        Date date = new Date();
        //设置要获取到什么样的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取String类型的时间
        String createdate = sdf.format(date);
        commentModel.setCommentdate(createdate);
        commentModel.setBlogid(blogid);
        commentModel.setCommentaterid(12);
        commentModel.setId(id);
        commentModel.setFathercommentid(fathercommentid);





        CommentModel commentModelForReturn=commentService.createComment(commentModel);
        CommentVO commentVO=convertFromModel(commentModelForReturn);

        return CommonReturnType.create(commentVO);
    }

    private CommentVO convertFromModel(CommentModel commentModel){
        if(commentModel==null){
            return null;
        }
        CommentVO commentVO = new CommentVO();
        BeanUtils.copyProperties(commentModel,commentVO);
        return commentVO;
    }



    @RequestMapping(value = "/getcomment",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType getComment(@RequestParam(name="blogid")Integer blogid
                                                ) {
        List<CommentModel> commentModelList=commentService.getCommentByBlogid(blogid);
        //使用stream api将list内的blogModel转化为BlogVO
        List<CommentVO> commentVOList=commentModelList.stream().map(commentModel -> {
            CommentVO commentVO=this.convertFromModel(commentModel);
            return commentVO;
        }).collect(Collectors.toList());

        return CommonReturnType.create(commentVOList);
    }

}
