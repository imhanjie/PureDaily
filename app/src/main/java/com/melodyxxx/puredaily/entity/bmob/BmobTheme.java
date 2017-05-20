package com.melodyxxx.puredaily.entity.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by hanjie on 2017/5/20.
 */

public class BmobTheme extends BmobObject{

    private String name;
    private long id;
    private int color;
    private String thumbnail;
    private String description;
    private String themeName;
    private long time;

    public BmobTheme(String name, long id, int color, String thumbnail, String description, String themeName, long time) {
        this.name = name;
        this.id = id;
        this.color = color;
        this.thumbnail = thumbnail;
        this.description = description;
        this.themeName = themeName;
        this.time = time;
    }

    public BmobTheme() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
