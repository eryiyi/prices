package com.xiaogang.myapp.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/4.
 */
public class Chengdu implements Serializable{
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Chengdu(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
