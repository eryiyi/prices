package com.xiaogang.myapp.data;

import com.xiaogang.myapp.model.GoodsTypeBean;

import java.util.List;

/**
 * Created by Administrator on 2015/10/1.
 */
public class GoodsTypeBeanData  extends Data{
    private List<GoodsTypeBean> data;

    public List<GoodsTypeBean> getData() {
        return data;
    }

    public void setData(List<GoodsTypeBean> data) {
        this.data = data;
    }
}
