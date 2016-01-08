package com.xiaogang.myapp.model;

/**
 * Created by Administrator on 2015/7/28.
 * ÁÄÌìÏûÏ¢
 */
public class Minemsg {
    private int pic;
    private String title;
    private String dateline;
    private String cont;

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

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public Minemsg(int pic, String title, String dateline, String cont) {
        this.pic = pic;
        this.title = title;
        this.dateline = dateline;
        this.cont = cont;
    }
}
