package com.xiaogang.myapp.model;

import com.xiaogang.myapp.data.Data;

/**
 * Created by Administrator on 2015/8/26.
 */
public class LoginData extends Data {
    private LoginSuccess data;

    public LoginSuccess getData() {
        return data;
    }

    public void setData(LoginSuccess data) {
        this.data = data;
    }
}
