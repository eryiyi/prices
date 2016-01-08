package com.xiaogang.myapp.data;

import com.xiaogang.myapp.model.SearchGoods;

import java.util.List;

/**
 * Created by Administrator on 2015/8/29.
 */
public class SearchGoodsData extends Data {
    private List<SearchGoods> data;

    public List<SearchGoods> getData() {
        return data;
    }

    public void setData(List<SearchGoods> data) {
        this.data = data;
    }
}
