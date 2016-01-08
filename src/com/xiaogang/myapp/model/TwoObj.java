package com.xiaogang.myapp.model;

import java.util.List;

/**
 * Created by Administrator on 2015/9/7.
 */
public class TwoObj {
    private String gid;
    private String title;
    private List<TwoObjGoods> goods;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TwoObjGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<TwoObjGoods> goods) {
        this.goods = goods;
    }
}
