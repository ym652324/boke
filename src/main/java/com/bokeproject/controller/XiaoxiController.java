package com.bokeproject.controller;


import com.bokeproject.controller.viewobject.XiaoxiVO;
import com.bokeproject.error.BusinessException;
import com.bokeproject.response.CommonReturnType;
import com.bokeproject.service.XiaoxiService;
import com.bokeproject.service.impl.model.XiaoxiModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
@Controller("xiaoxi")
@RequestMapping("/xiaoxi")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class XiaoxiController extends BaseController {
    @Autowired
    XiaoxiService xiaoxiService;

    @RequestMapping(value = "/create",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType createMessage(@RequestParam(name="acceptid")Integer acceptid,
                                          @RequestParam(name="blogormessageid")Integer blogormessageid,
                                          @RequestParam(name="xiaoxi")String xiaoxi,
                                          @RequestParam(name="type")String type,
                                          @RequestParam(name="sendid")Integer sendid

    ) throws BusinessException {
        //封装Service请求用来创建博客
//        System.out.println(acceptid);
//        System.out.println(blogormessageid);

        XiaoxiModel xiaoxiModel=new XiaoxiModel();
        xiaoxiModel.setAcceptid(acceptid);
        Date date = new Date();
        //设置要获取到什么样的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取String类型的时间
        String createdate = sdf.format(date);
        xiaoxiModel.setXiaoxidate(createdate);
        xiaoxiModel.setBlogormessageid(blogormessageid);
        xiaoxiModel.setType(type);
        xiaoxiModel.setXiaoxi(xiaoxi);
        xiaoxiModel.setSendid(sendid);
        XiaoxiModel xiaoxiModelForReturn=xiaoxiService.createXiaoxi(xiaoxiModel);
        XiaoxiVO xiaoxiVO=convertFromModel(xiaoxiModelForReturn);


        return CommonReturnType.create(xiaoxiVO);
    }

    private XiaoxiVO convertFromModel(XiaoxiModel xiaoxiModel){
        if(xiaoxiModel==null){
            return null;
        }
        XiaoxiVO xiaoxiVO = new XiaoxiVO();
        BeanUtils.copyProperties(xiaoxiModel,xiaoxiVO);
        return xiaoxiVO;
    }

    @RequestMapping(value = "/listxiaoxi",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType listXiaoxi(@RequestParam(name="acceptid")Integer acceptid) {
        List<XiaoxiModel> xiaoxiModelList=xiaoxiService.listXiaoxi(acceptid);
        //使用stream api将list内的blogModel转化为BlogVO
        if(xiaoxiModelList==null||xiaoxiModelList.size()==0){
            System.out.println("list empty");
            return null;
        }
        List<XiaoxiVO> xiaoxiVOList=xiaoxiModelList.stream().map(xiaoxiModel -> {
            if(xiaoxiModel==null){
                System.out.println("empty");
            }
            else{
                System.out.println(xiaoxiModel.getSendman());
            }
            XiaoxiVO xiaoxiVO=this.convertFromModel(xiaoxiModel);
            return xiaoxiVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(xiaoxiVOList);
    }




}
