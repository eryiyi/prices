package com.xiaogang.myapp.data;

import com.xiaogang.myapp.model.GoodDetailSalesBaojia;

import java.util.List;

/**
 * Created by Administrator on 2015/10/4.
 *  "sh": "4000",
 "sid": "2",
 "sa": {
 "address": "�������山����ͯ·",
 "level": "1",
 "mobile": "53515326",
 "name": "�山ƻ��ר����"
 },
 "hh": "5000"
 */
public class GoodDetailSalesBaojiaData  extends Data{
    private List<GoodDetailSalesBaojia> data;

    public List<GoodDetailSalesBaojia> getData() {
        return data;
    }

    public void setData(List<GoodDetailSalesBaojia> data) {
        this.data = data;
    }
}
