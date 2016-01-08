package com.xiaogang.myapp.model;

/**
 * Created by Administrator on 2015/7/28.
 */
public class ItemGrid {
    private int pic;
    private String title;

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

    public ItemGrid(int pic, String title) {
        this.pic = pic;
        this.title = title;
    }
}
