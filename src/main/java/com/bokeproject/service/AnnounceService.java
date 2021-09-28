package com.bokeproject.service;

import com.bokeproject.error.BusinessException;
import com.bokeproject.service.impl.model.AnnounceModel;
import com.bokeproject.service.impl.model.BlogModel;

import java.util.List;

public interface AnnounceService {


    //公告列表浏览
    List<AnnounceModel> listAnnounce();

    //浏览量统计
    AnnounceModel clickAnnounce(AnnounceModel announceModel)throws BusinessException;

    AnnounceModel getAnnounceByAnnounceid(Integer announceid);

    AnnounceModel addAnnounce(AnnounceModel announceModel) throws BusinessException;
    List<AnnounceModel> searchAnnounce(String shuru);
    boolean deleteAnnounceById(Integer[] arr);
    List<AnnounceModel> pagechange(Integer start);
}
