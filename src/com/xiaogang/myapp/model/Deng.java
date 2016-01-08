package com.xiaogang.myapp.model;

/**
 * Created by Administrator on 2015/7/27.
 */
public class Deng {
    private int pic;
    private String title;
    private String jiage;

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJiage() {
        return jiage;
    }

    public void setJiage(String jiage) {
        this.jiage = jiage;
    }

    public Deng(int pic, String title, String jiage) {
        this.pic = pic;
        this.title = title;
        this.jiage = jiage;
    }
}
