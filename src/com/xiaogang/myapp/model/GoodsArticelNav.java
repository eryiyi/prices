package com.xiaogang.myapp.model;

import java.util.List;

/**
 * Created by Administrator on 2015/8/27.
 */
public class GoodsArticelNav {
    private String id;
    private String nav_name;
    private List<GoodsArticel> goods;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNav_name() {
        return nav_name;
    }

    public void setNav_name(String nav_name) {
        this.nav_name = nav_name;
    }

    public List<GoodsArticel> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsArticel> goods) {
        this.goods = goods;
    }
}
