package com.xiaogang.myapp.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/30.
 */
public class ShangdianType implements Serializable{
    private String id;
    private String nav_name;

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
}
