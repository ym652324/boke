package com.bokeproject.controller;

import com.bokeproject.controller.viewobject.BlogVO;
import com.bokeproject.controller.viewobject.SaveVO;
import com.bokeproject.response.CommonReturnType;
import com.bokeproject.service.BlogService;
import com.bokeproject.service.SaveService;
import com.bokeproject.service.impl.model.BlogModel;
import com.bokeproject.service.impl.model.SaveModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller("save")
@RequestMapping("/save")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class SaveController extends BaseController{
    @Autowired
    SaveService  saveService;

    @Autowired
    BlogService blogService;


    @RequestMapping(value = "/listUserSave",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType listusersave(@RequestParam(name="id")Integer id){
        List<Integer> BlogIdList=saveService.listUserSaveBlog(id);
        if(BlogIdList==null){
            return CommonReturnType.create(null);
        }
        List<BlogModel>blogModelList=blogService.listUserSave(BlogIdList);
        return CommonReturnType.create(blogModelList);
    }

    @RequestMapping(value = "/saveBlog",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType saveBlog(@RequestParam(name="blogid")Integer blogid,
                                     @RequestParam(name="id")Integer id){
        SaveModel saveModel=new SaveModel();
        saveModel.setBlogid(blogid);
        saveModel.setId(id);
        if(!saveService.selectByIdandBlogId(id,blogid)){
            saveService.saveBlog(saveModel);
            return CommonReturnType.create(null);
        }

        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/cancelSaveBlog",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType cancelSaveBlog(@RequestParam(name="blogid")Integer blogid,
                                     @RequestParam(name="id")Integer id){
        saveService.cancelSaveBlog(blogid,id);
        return CommonReturnType.create(null);
    }

}
