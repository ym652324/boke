package com.bokeproject.controller;

import com.bokeproject.response.CommonReturnType;
import com.bokeproject.service.BlogService;
import com.bokeproject.service.SaveService;
import com.bokeproject.service.ZanService;
import com.bokeproject.service.impl.model.BlogModel;
import com.bokeproject.service.impl.model.SaveModel;
import com.bokeproject.service.impl.model.ZanModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("zan")
@RequestMapping("/zan")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ZanController extends BaseController{
    @Autowired
    ZanService zanService;

    @Autowired
    BlogService blogService;


    @RequestMapping(value = "/listUserZan",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType listuserzan(@RequestParam(name="id")Integer id){
        List<Integer> BlogIdList=zanService.listUserZanBlog(id);
        if(BlogIdList==null){
            return CommonReturnType.create(null);
        }
        List<BlogModel>blogModelList=blogService.listUserZan(BlogIdList);
        return CommonReturnType.create(blogModelList);
    }

    @RequestMapping(value = "/zanBlog",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType saveBlog(@RequestParam(name="blogid")Integer blogid,
                                     @RequestParam(name="id")Integer id){
        ZanModel zanModel=new ZanModel();
        zanModel.setBlogid(blogid);
        zanModel.setId(id);
        if(!zanService.selectByIdandBlogId(id,blogid)){
            zanService.zanBlog(zanModel);
            return CommonReturnType.create(null);
        }

        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/cancelZanBlog",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType cancelSaveBlog(@RequestParam(name="blogid")Integer blogid,
                                           @RequestParam(name="id")Integer id){
        zanService.cancelZanBlog(blogid,id);
        return CommonReturnType.create(null);
    }
}
