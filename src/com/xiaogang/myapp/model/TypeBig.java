package com.xiaogang.myapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/8/29.
 */
public class TypeBig implements Parcelable {
    private String id;
    private String nav_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNav_name() {
        return nav_name;
    }

    public void setNav_name(String nav_name) {
        this.nav_name = nav_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
