package com.xiaogang.myapp.model;

/**
 * Created by Administrator on 2015/8/29.
 *  "sh": "4000",
 "sid": "2",
 "sa": {
 "address": "重庆市渝北区金童路",
 "level": "1",
 "mobile": "53515326",
 "name": "渝北苹果专卖店"
 },
 "hh": "5000"
 */
public class GoodDetailSalesBaojia {
    private String sid;
    private String hh;
    private String sh;
    private GoodDetailSalesBaojiaSa sa;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getHh() {
        return hh;
    }

    public void setHh(String hh) {
        this.hh = hh;
    }

    public String getSh() {
        return sh;
    }

    public void setSh(String sh) {
        this.sh = sh;
    }

    public GoodDetailSalesBaojiaSa getSa() {
        return sa;
    }

    public void setSa(GoodDetailSalesBaojiaSa sa) {
        this.sa = sa;
    }
}
