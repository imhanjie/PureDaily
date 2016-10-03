package com.melodyxxx.puredaily.entity.daily;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  2016/6/6.
 * Description:
 */
@Entity
public class Collection {
    @Id
    private long id;
    private String title;
    private String imgUrl;
    private long time;

    @Generated(hash = 1150457855)
    public Collection(long id, String title, String imgUrl, long time) {
        this.id = id;
        this.title = title;
        this.imgUrl = imgUrl;
        this.time = time;
    }

    @Generated(hash = 1149123052)
    public Collection() {
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", time=" + time +
                '}';
    }
}
