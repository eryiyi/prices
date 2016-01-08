package com.xiaogang.myapp.model;

import java.util.List;

/**
 * Created by Administrator on 2015/8/29.
 */
public class GoodsTypeSmall {
    private String id;
    private String pid;
    private String nav_name;
    private String path;
    private String bpath;
    private String count;
    private List<GoodsTypeSmallChild> child;

    public List<GoodsTypeSmallChild> getChild() {
        return child;
    }

    public void setChild(List<GoodsTypeSmallChild> child) {
        this.child = child;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getNav_name() {
        return nav_name;
    }

    public void setNav_name(String nav_name) {
        this.nav_name = nav_name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBpath() {
        return bpath;
    }

    public void setBpath(String bpath) {
        this.bpath = bpath;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
