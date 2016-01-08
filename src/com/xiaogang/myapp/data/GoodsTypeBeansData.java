package com.xiaogang.myapp.data;

import com.xiaogang.myapp.model.GoodsTypeBean;
import com.xiaogang.myapp.model.GoodsTypeBeans;

import java.util.List;

/**
 * Created by Administrator on 2015/10/1.
 */
public class GoodsTypeBeansData extends Data{
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
