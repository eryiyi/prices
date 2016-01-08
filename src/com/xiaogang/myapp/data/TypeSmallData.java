package com.xiaogang.myapp.data;

import com.xiaogang.myapp.model.GoodsTypeSmall;

import java.util.List;

/**
 * Created by Administrator on 2015/8/29.
 */
public class TypeSmallData extends Data {
    private List<GoodsTypeSmall> data;

    public List<GoodsTypeSmall> getData() {
        return data;
    }

    public void setData(List<GoodsTypeSmall> data) {
        this.data = data;
    }
}
