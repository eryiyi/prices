package com.xiaogang.myapp.data;

import com.xiaogang.myapp.model.NearByGoods;

import java.util.List;

/**
 * Created by Administrator on 2015/10/18.
 */
public class NearByGoodsData extends Data {
    private List<NearByGoods> data;

    public List<NearByGoods> getData() {
        return data;
    }

    public void setData(List<NearByGoods> data) {
        this.data = data;
    }
}
