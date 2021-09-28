package com.bokeproject.controller;


import com.bokeproject.controller.viewobject.AnnounceVO;
import com.bokeproject.controller.viewobject.BlogVO;
import com.bokeproject.controller.viewobject.UserVO;
import com.bokeproject.error.BusinessException;
import com.bokeproject.response.CommonReturnType;
import com.bokeproject.service.AnnounceService;
import com.bokeproject.service.impl.model.AnnounceModel;
import com.bokeproject.service.impl.model.BlogModel;
import com.bokeproject.service.impl.model.DataReturnModel;
import com.bokeproject.service.impl.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller("announce")
@RequestMapping("/announce")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class AnnounceController extends BaseController {

    @Autowired
    private AnnounceService announceService;


    @RequestMapping(value = "/listannounce",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listannounce(){
        List<AnnounceModel>announceModelList=announceService.listAnnounce();
        List<AnnounceVO>announceVOList=announceModelList.stream().map(announceModel -> {
            AnnounceVO announceVO=this.convertFromModel(announceModel);
            return announceVO;
        }).collect(Collectors.toList());
        DataReturnModel dataReturnModel=new DataReturnModel<AnnounceVO>();
        dataReturnModel.setLength(announceModelList.size());
        if(announceVOList.size()<8){
            dataReturnModel.setData(announceVOList.subList(0,announceVOList.size()));
        }
        else{
            dataReturnModel.setData(announceVOList.subList(0,8));
        }
        return CommonReturnType.create(dataReturnModel);
    }



    @RequestMapping(value = "/click",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType click(@RequestParam(name="announceid")Integer announceid,
                                  @RequestParam(name="announceclick")Integer announceclick) throws BusinessException {
        AnnounceModel announceModel = announceService.getAnnounceByAnnounceid(announceid);
        announceModel.setAnnounceclick(announceclick);
        AnnounceModel announceModelForReturn=announceService.clickAnnounce(announceModel);
        AnnounceVO announceVO=convertFromModel(announceModelForReturn);

        return CommonReturnType.create(announceVO);
    }


    private AnnounceVO convertFromModel(AnnounceModel announceModel){
        if(announceModel==null){
            return null;
        }
        AnnounceVO announceVO = new AnnounceVO();
        BeanUtils.copyProperties(announceModel,announceVO);
        return announceVO;
    }

    @RequestMapping(value = "/addAnnounce",method = {RequestMethod.POST},consumes = {CONTENT_TRPE_FORMED})
    @ResponseBody
    public CommonReturnType addAnnounce(@RequestParam(name="title")String title,
                                     @RequestParam(name="announcements")String announcements,
                                        @RequestParam(name="id")Integer id
    ) throws BusinessException {

        AnnounceModel announceModel =new AnnounceModel();
        announceModel.setAnnouncetitle(title);
        announceModel.setAnnouncements(announcements);
        Date date = new Date();
        //设置要获取到什么样的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取String类型的时间
        String createdate = sdf.format(date);
        announceModel.setAnnouncedate(createdate);
        announceModel.setId(id);


        AnnounceModel announceModelForReturn=announceService.addAnnounce(announceModel);
        AnnounceVO announceVO=convertFromModel(announceModelForReturn);
        return CommonReturnType.create(announceVO);

    }


    @RequestMapping(value = "/search",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType search(@RequestParam(name="shuru")String shuru){
        List<AnnounceModel> announceModelList=announceService.searchAnnounce(shuru);
        //使用stream api将list内的blogModel转化为BlogVO
        List<AnnounceVO>announceVOList=announceModelList.stream().map(announceModel -> {
            AnnounceVO announceVO=this.convertFromModel(announceModel);
            return announceVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(announceVOList);

    }

    @RequestMapping(value = "/deleteAnnounce",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType deleteAnnounce(@RequestParam(name="ids")String ids){

        String[] str=ids.split("-");
        Integer[] arr=new Integer[str.length];

        for(Integer i=0;i<str.length;i++){
            arr[i]=Integer.parseInt(str[i]);
        }

        boolean isSuccess=announceService.deleteAnnounceById(arr);

        if(isSuccess){
            return listannounce();
        }
        return CommonReturnType.create(null,"fail");

    }


    @RequestMapping(value = "/pagechange",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType pagechange(@RequestParam(name="page")Integer page) {
        Integer start=(page-1)*8;
        List<AnnounceModel> announceModelList=announceService.pagechange(start);
        //使用stream api将list内的blogModel转化为BlogVO
        List<AnnounceVO> announceVOList=announceModelList.stream().map(announceModel -> {
            AnnounceVO announceVO=this.convertFromModel(announceModel);
            return announceVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(announceVOList);
    }


}
