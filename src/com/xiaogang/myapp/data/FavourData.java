package com.xiaogang.myapp.data;

import com.xiaogang.myapp.model.Favour;

import java.util.List;

/**
 * Created by Administrator on 2015/9/20.
 */
public class FavourData extends Data {
    private List<Favour> data;

    public List<Favour> getData() {
        return data;
    }

    public void setData(List<Favour> data) {
        this.data = data;
    }
}
