package com.melodyxxx.puredaily.entity.bmob;


import cn.bmob.v3.BmobObject;

/**
 * Created by hanjie on 2017/4/16.
 */

public class BmobUser extends BmobObject {

    private String name;
    private String pwd;

    public BmobUser(String name, String pwd) {
        this.name = name;
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
