package com.xiaogang.myapp.data;

import com.xiaogang.myapp.model.GoodsSuggest;

import java.util.List;

/**
 * Created by Administrator on 2015/9/2.
 */
public class GoodsSuggestData  extends Data{
    private List<GoodsSuggest> data;

    public List<GoodsSuggest> getData() {
        return data;
    }

    public void setData(List<GoodsSuggest> data) {
        this.data = data;
    }
}
