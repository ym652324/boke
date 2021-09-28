package com.bokeproject.controller;

import com.bokeproject.controller.viewobject.UserVO;
import com.bokeproject.error.BusinessException;
import com.bokeproject.error.EmBusinessError;
import com.bokeproject.response.CommonReturnType;
import com.bokeproject.service.AnnounceService;
import com.bokeproject.service.UserService;
import com.bokeproject.service.impl.model.AnnounceModel;
import com.bokeproject.service.impl.model.DataReturnModel;
import com.bokeproject.service.impl.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.valueOf;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;

    @Autowired
    private AnnounceService announceService;

    @Autowired
    private HttpServletRequest httpServletRequest;
    //用户登陆接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TRPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telphone")String telphone,
                                  @RequestParam(name="passwordJM")String passwordJM) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
  //      入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telphone)||
            StringUtils.isEmpty(passwordJM)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //用户登录服务，用来校验用户是否合法
        UserModel userModel = userService.validateLogin(telphone,this.EncoderByMd5(passwordJM));
        //将登录凭证加入到用户成功登陆的session内

        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        return CommonReturnType.create(userModel);
    }

    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TRPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telphone")String telphone,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="gender")Integer gender,
                                     @RequestParam(name="age")Integer age,
                                     @RequestParam(name="iconurl")String iconurl,
                                     @RequestParam(name="passwordJM")String passwordJM) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        if(userService.selectByTelphone(telphone)!=null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"该用户已经存在！");
        }

        //验证手机号和对应的otpcode相符合
        String inSessionOtpCode=(String)this.httpServletRequest.getSession().getAttribute(telphone);
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        //用户的注册流程
        UserModel userModel =new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setPassword(this.EncoderByMd5(passwordJM));
        userModel.setIconurl(iconurl);
        userModel.setIsadmin(new Byte(String.valueOf('0')));

        userService.register(userModel);
        return CommonReturnType.create(null);

    }
    public String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定一个计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密字符串
        String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }




    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TRPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telphone")String telphone){
        //需要按照一定的规则生成OTP验证码
        Random random=new Random();
        int randomInt = random.nextInt(99999);
        randomInt +=10000;
        String otpCode = String.valueOf(randomInt);

        //将OTP验证码通对应的用户手机号关联，使用httpsession的方法绑定他的手机号与OTPcode
        httpServletRequest.getSession().setAttribute(telphone,otpCode);

        //将OTP验证码通过短信通道发送给用户，此处为模拟
        System.out.println("telphone = "+telphone+" &otpCode = "+otpCode);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
        @ResponseBody
        public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
            //调用service服务获取到对应id的用户对象并返回给前端
            UserModel userModel=userService.getUserById(id);

            //若获取的对应用户信息不存在
            if(userModel==null){
                throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
            }

        //将核心领域用户对象转化为可供UI使用的viewobject
        UserVO userVO= convertFromModel(userModel);
        //返回通用对象
        return CommonReturnType.create(userVO);
    }

    @RequestMapping(value = "/listuser",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType listuser(@RequestParam(name="isAdmin")Integer isAdmin){
        List<UserModel> userModelList=userService.listUser(isAdmin);
        //使用stream api将list内的blogModel转化为BlogVO
        List<UserVO> userVOList=userModelList.stream().map(userModel -> {
            UserVO userVO=this.convertFromModel(userModel);
            return userVO;
        }).collect(Collectors.toList());
        DataReturnModel dataReturnModel=new DataReturnModel<UserVO>();
        dataReturnModel.setLength(userVOList.size());
        if(userVOList.size()<8){
            dataReturnModel.setData(userVOList.subList(0,userVOList.size()));
        }
        else{
            dataReturnModel.setData(userVOList.subList(0,8));
        }
        return CommonReturnType.create(dataReturnModel);
    }

    @RequestMapping(value = "/deleteUser",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType deleteUser(@RequestParam(name="ids")String ids,
                                        @RequestParam(name="isAdmin")Integer isAdmin){
        String[] str=ids.split("-");
        Integer[] arr=new Integer[str.length];

        for(Integer i=0;i<str.length;i++){
            arr[i]=Integer.parseInt(str[i]);
        }
        boolean isSuccess=userService.deleteUserById(arr);
        if(isSuccess){
            return listuser(isAdmin);
        }
        return CommonReturnType.create(null,"fail");

    }

    @RequestMapping(value = "/toBeAdmin",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType toBeAdmin(@RequestParam(name="ids")String ids){
        String[] str=ids.split("-");
        Integer[] arr=new Integer[str.length];

        for(Integer i=0;i<str.length;i++){
            arr[i]=Integer.parseInt(str[i]);
        }
        boolean isSuccess=userService.toBeAdmin(arr);

        if(isSuccess){
            return listuser(0);
        }
        return CommonReturnType.create(null,"fail");

    }

    @RequestMapping(value = "/pagechange",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType pagechange(@RequestParam(name="page")Integer page,
                                       @RequestParam(name="isAdmin")Integer isAdmin) {
            Integer start=(page-1)*8;
            List<UserModel> userModelList=userService.pagechange(start,isAdmin);
            //使用stream api将list内的blogModel转化为BlogVO
            List<UserVO> userVOList=userModelList.stream().map(userModel -> {
                UserVO userVO=this.convertFromModel(userModel);
                return userVO;
            }).collect(Collectors.toList());
            return CommonReturnType.create(userVOList);
    }

    private UserVO convertFromModel(UserModel userModel){
        if(userModel==null){
            return null;
        }
        UserVO userVO=new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }

    //用户浏览文章
//    @RequestMapping(value = "/read",method = {RequestMethod.POST},consumes = {CONTENT_TRPE_FORMED})
//    @ResponseBody

    @RequestMapping(value = "/telSearch",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType telSearch(@RequestParam(name="telphone")String telphone) throws BusinessException {
        UserModel userModel=userService.selectByTelphone(telphone);
        //使用stream api将list内的blogModel转化为BlogVO
        if(userModel==null){
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }

        //将核心领域用户对象转化为可供UI使用的viewobject
        UserVO userVO= convertFromModel(userModel);
        //返回通用对象
        return CommonReturnType.create(userVO);

    }

    @RequestMapping(value = "/nameSearch",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType nameSearch(@RequestParam(name="name")String name) throws BusinessException {
        List<UserModel> userModelList=userService.selectByName(name);
        //使用stream api将list内的blogModel转化为BlogVO
        List<UserVO> userVOList=userModelList.stream().map(userModel -> {
            UserVO userVO=this.convertFromModel(userModel);
            return userVO;
        }).collect(Collectors.toList());
        DataReturnModel dataReturnModel=new DataReturnModel<UserVO>();
        dataReturnModel.setLength(userVOList.size());
        if(userVOList.size()<8){
            dataReturnModel.setData(userVOList.subList(0,userVOList.size()));
        }
        else{
            dataReturnModel.setData(userVOList.subList(0,8));
        }
        return CommonReturnType.create(dataReturnModel);

    }

    @RequestMapping(value = "/addUser",method = {RequestMethod.POST},consumes = {CONTENT_TRPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telphone")String telphone,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="gender")Integer gender,
//                                     @RequestParam(name="age")Integer age,
                                     @RequestParam(name="icon")String icon,
                                     @RequestParam(name="isAdmin")Integer isAdmin,
                                     @RequestParam(name="passwordJM")String passwordJM) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和对应的otpcode相符合

        //用户的注册流程
        UserModel userModel =new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(15);
        userModel.setTelphone(telphone);
        userModel.setPassword(this.EncoderByMd5(passwordJM));
        userModel.setIconurl(icon);

        userModel.setIsadmin(new Byte(String.valueOf(isAdmin)));

        userService.register(userModel);
        return CommonReturnType.create(null);

    }

    @RequestMapping(value = "/icon",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType upload(@RequestParam(name="imgurl") MultipartFile imgurl, HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
        //String path = request.getSession().getServletContext().getRealPath("upload");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式  HH:mm:ss
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
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




}


