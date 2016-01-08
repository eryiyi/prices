package com.xiaogang.myapp.data;

import java.util.List;

/**
 * Created by Administrator on 2015/8/26.
 */
public class CardData extends Data{
    private List<Code> data;

    public List<Code> getData() {
        return data;
    }

    public void setData(List<Code> data) {
        this.data = data;
    }
}
