package com.xiaogang.myapp.data;

import com.xiaogang.myapp.model.News;

import java.util.List;

/**
 * Created by Administrator on 2015/8/26.
 */
public class NewsData extends Data{
    private List<News> data;

    public List<News> getData() {
        return data;
    }

    public void setData(List<News> data) {
        this.data = data;
    }
}
