package com.xiaogang.myapp.data;

import com.xiaogang.myapp.model.Articel;

import java.util.List;

/**
 * Created by Administrator on 2015/9/7.
 */
public class ArticelData extends Data {
    private List<Articel> data;

    public List<Articel> getData() {
        return data;
    }

    public void setData(List<Articel> data) {
        this.data = data;
    }
}
