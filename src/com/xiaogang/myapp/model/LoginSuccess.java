package com.xiaogang.myapp.model;

/**
 * Created by Administrator on 2015/8/26.
 */
public class LoginSuccess  {
    private String uid;
    private String user;
    private String access_token;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
