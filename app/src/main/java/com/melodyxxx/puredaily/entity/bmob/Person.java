package com.melodyxxx.puredaily.entity.bmob;


import cn.bmob.v3.BmobObject;

/**
 * Created by hanjie on 2017/4/16.
 */

public class Person extends BmobObject {

    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
