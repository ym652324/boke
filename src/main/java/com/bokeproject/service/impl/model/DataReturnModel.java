package com.bokeproject.service.impl.model;

import com.bokeproject.controller.viewobject.UserVO;

import java.util.List;

public class DataReturnModel<TYPE> {
    private Integer length;
    private List<TYPE> data;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<TYPE> getData() {
        return data;
    }

    public void setData(List<TYPE> data) {
        this.data = data;
    }
}