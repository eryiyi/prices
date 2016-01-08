package com.xiaogang.myapp.data;

import com.xiaogang.myapp.model.GoodsComment;

import java.util.List;

/**
 * Created by Administrator on 2015/8/29.
 */
public class GoodsCommentData extends Data {
    private List<GoodsComment> data;

    public List<GoodsComment> getData() {
        return data;
    }

    public void setData(List<GoodsComment> data) {
        this.data = data;
    }
}
