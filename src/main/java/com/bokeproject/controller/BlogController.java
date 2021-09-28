package com.bokeproject.controller;

import com.bokeproject.controller.viewobject.BlogVO;
import com.bokeproject.controller.viewobject.SaveVO;
import com.bokeproject.error.BusinessException;
import com.bokeproject.response.CommonReturnType;
import com.bokeproject.service.BlogService;
import com.bokeproject.service.impl.model.BlogModel;
import com.bokeproject.service.impl.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller("blog")
@RequestMapping("/blog")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class BlogController extends BaseController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    //创建商品的controller
    @RequestMapping(value = "/create",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType createBlog(@RequestParam(name="blogtitle")String blogtitle,
                                       @RequestParam(name="blogs")String blogs,
                                       @RequestParam(name="id")Integer id
                                        ) throws BusinessException{
        //封装Service请求用来创建博客
        BlogModel blogModel=new BlogModel();
        blogModel.setBlogtitle(blogtitle);
        blogModel.setBlogs(blogs);
        Date date = new Date();
        //设置要获取到什么样的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取String类型的时间
        String createdate = sdf.format(date);
        blogModel.setBlogdate(createdate);
        blogModel.setChangedate(createdate);

        blogModel.setId(id);
        blogModel.setZan(0);

        BlogModel blogModelForReturn=blogService.createBlog(blogModel);
        BlogVO blogVO=convertFromModel(blogModelForReturn);
        return CommonReturnType.create(blogVO);
    }

    @RequestMapping(value = "/xiugai",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType xiugaiBlog(@RequestParam(name="blogtitle")String blogtitle,
                                       @RequestParam(name="blogs")String blogs,
                                       @RequestParam(name="blogid")Integer blogid,
                                       @RequestParam(name="id")Integer id,
                                       @RequestParam(name="click")Integer click,
                                       @RequestParam(name="zan")Integer zan,
                                       @RequestParam(name="commentnum")Integer commentnum,
                                       @RequestParam(name="blogdate")String blogdate
    ) throws BusinessException{
        //封装Service请求用来创建博客

        BlogModel blogModel = new BlogModel();
        blogModel.setBlogtitle(blogtitle);
        blogModel.setBlogs(blogs);
        blogModel.setCommentnum(commentnum);
        blogModel.setClick(click);
        blogModel.setZan(zan);
        blogModel.setBlogdate(blogdate);
        blogModel.setBlogid(blogid);
        blogModel.setId(id);
        Date date = new Date();
        //设置要获取到什么样的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取String类型的时间
        String createdate = sdf.format(date);
        blogModel.setChangedate(createdate);
        Boolean blogModelForReturn=blogService.xiugai(blogModel);

        if(blogModelForReturn){
            return CommonReturnType.create(null);
        }
        return CommonReturnType.create("fail");
    }


    @RequestMapping(value = "/image",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType upload(@RequestParam(name="imgurl") MultipartFile imgurl, HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException{
        //String path = request.getSession().getServletContext().getRealPath("upload");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String date = df.format(new Date());
        String path = "E:/Java/project/img/"+date;
        UUID uuid=UUID.randomUUID();
        String originalFilename = imgurl.getOriginalFilename();
        // String fileName = uuid.toString() + originalFilename;
        String extendName = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
        String fileName = uuid.toString() + extendName;
        File dir = new File(path, fileName);
        File filepath = new File(path);
        if(!filepath.exists()){
            filepath.mkdirs();
        }
        imgurl.transferTo(dir);
        Map<String, String> map = new HashMap<>();
        map.put("filePath", path);
        map.put("fileName", fileName);
        return CommonReturnType.create(map);
    }


    @RequestMapping(value = "/zan",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType zan(@RequestParam(name="blogid")Integer blogid,
                                @RequestParam(name="zan")Integer zan) throws BusinessException {
        BlogModel blogModel = blogService.getBlogByBlogid(blogid);
        blogModel.setZan(zan);
        BlogModel blogModelForReturn=blogService.zanBlog(blogModel);
        BlogVO blogVO=convertFromModel(blogModelForReturn);
        return CommonReturnType.create(blogVO);
    }


    @RequestMapping(value = "/click",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType click(@RequestParam(name="blogid")Integer blogid,
                                @RequestParam(name="click")Integer click) throws BusinessException {
        BlogModel blogModel = blogService.getBlogByBlogid(blogid);
        blogModel.setClick(click);
        BlogModel blogModelForReturn=blogService.clickBlog(blogModel);
        BlogVO blogVO=convertFromModel(blogModelForReturn);
        return CommonReturnType.create(blogVO);
    }


    @RequestMapping(value = "/getblog",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType getblog(@RequestParam(name="blogid")Integer blogid){
        BlogModel blogModel = blogService.getBlogByBlogid(blogid);
        BlogVO blogVO=convertFromModel(blogModel);
        return CommonReturnType.create(blogVO);
    }
    @RequestMapping(value = "/deleteBlog",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType deleteBlog(@RequestParam(name="blogid")Integer blogid){
        Boolean isSuccess=blogService.deleteBlog(blogid);
        if(isSuccess){
            return CommonReturnType.create(null);
        }
        return CommonReturnType.create("fail");
    }

    @RequestMapping(value = "/listUserBlog",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType listuserblog(@RequestParam(name="id")Integer id){
//        System.out.println(id);
        List<BlogModel> blogModelList=blogService.listUserBlog(id);
        //使用stream api将list内的blogModel转化为BlogVO
        List<BlogVO> blogVOList=blogModelList.stream().map(blogModel -> {
            BlogVO blogVO=this.convertFromModel(blogModel);
            return blogVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(blogVOList);
    }

    @RequestMapping(value = "/listUserSave",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType listusersave(@RequestParam(name="BlogIdList")List<Integer> BlogIdList){
//        List<SaveModel> saveModelList=saveService.listUserSave(id);
        List<BlogModel> blogModelList=blogService.listUserSave(BlogIdList);
        //使用stream api将list内的blogModel转化为BlogVO
        List<BlogVO> blogVOList=blogModelList.stream().map(blogModel -> {
            BlogVO blogVO=this.convertFromModel(blogModel);
            return blogVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(blogVOList);
    }


    @RequestMapping(value = "/listblog",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listblog(){
        List<BlogModel> blogModelList=blogService.listBlog();
//        System.out.println("Controller层blogModelLidtSize="+blogModelList.size());
        //使用stream api将list内的blogModel转化为BlogVO
        List<BlogVO> blogVOList=blogModelList.stream().map(blogModel -> {
            BlogVO blogVO=this.convertFromModel(blogModel);
            return blogVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(blogVOList);
    }


    @RequestMapping(value = "/search",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType search(@RequestParam(name="shuru")String shuru){
        List<BlogModel> blogModelList=blogService.searchBlog(shuru);
        //使用stream api将list内的blogModel转化为BlogVO
        List<BlogVO>blogVOList=blogModelList.stream().map(blogModel -> {
            BlogVO blogVO=this.convertFromModel(blogModel);
            return blogVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(blogVOList);

    }



    private BlogVO convertFromModel(BlogModel blogModel){
        if(blogModel==null){
            return null;
        }
        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blogModel,blogVO);
        return blogVO;
    }
}
