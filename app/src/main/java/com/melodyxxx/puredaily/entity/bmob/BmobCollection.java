package com.melodyxxx.puredaily.entity.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by hanjie on 2017/5/20.
 */

public class BmobCollection extends BmobObject {

    private String name;
    private long id;
    private String title;
    private String imageUrl;
    private long time;

    public BmobCollection() {
    }

    public BmobCollection(String userName, long id, String title, String imageUrl, long time) {
        this.name = userName;
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
