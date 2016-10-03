package com.melodyxxx.puredaily.entity.daily;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/30.
 * Description:
 */

@Entity
public class Theme {

    @Id
    public long id;
    public int color;
    public String thumbnail;
    public String description;
    public String name;
    public long time;

    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getThumbnail() {
        return this.thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public int getColor() {
        return this.color;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    @Generated(hash = 979395261)
    public Theme(long id, int color, String thumbnail, String description,
            String name, long time) {
        this.id = id;
        this.color = color;
        this.thumbnail = thumbnail;
        this.description = description;
        this.name = name;
        this.time = time;
    }
    @Generated(hash = 979000295)
    public Theme() {
    }

}
