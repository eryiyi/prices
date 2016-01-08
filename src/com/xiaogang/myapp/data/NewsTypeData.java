package com.xiaogang.myapp.data;

import com.xiaogang.myapp.model.NewsType;

import java.util.List;

/**
 * Created by Administrator on 2015/8/26.
 */
public class NewsTypeData extends Data{
    private List<NewsType> data;

    public List<NewsType> getData() {
        return data;
    }

    public void setData(List<NewsType> data) {
        this.data = data;
    }
}
