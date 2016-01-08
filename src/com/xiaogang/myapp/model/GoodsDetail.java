package com.xiaogang.myapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/8/29.
 */
public class GoodsDetail implements Serializable,Parcelable{
    private String id;
    private String content;
    private String name;//"
    private String nav;//"�����ֻ�",
    private String attr;//��ɫ:����;����:16G",
    private String price_sale;
    private String thumbnail;
    private String weight;// "0.50",
    private String evel;//"1"
    private String used;// "0",
    private String nav_name;// "0",

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNav_name() {
        return nav_name;
    }

    public void setNav_name(String nav_name) {
        this.nav_name = nav_name;
    }

    private String collect;// 0Ϊ�ղ�  1�ղ�


    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

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

        public String getNav() {
            return nav;
        }

        public void setNav(String nav) {
            this.nav = nav;
        }

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }

        public String getPrice_sale() {
            return price_sale;
        }

        public void setPrice_sale(String price_sale) {
            this.price_sale = price_sale;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getEvel() {
            return evel;
        }

        public void setEvel(String evel) {
            this.evel = evel;
        }

        public String getUsed() {
            return used;
        }

        public void setUsed(String used) {
            this.used = used;
        }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
