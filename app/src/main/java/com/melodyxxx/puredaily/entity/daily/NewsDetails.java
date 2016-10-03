package com.melodyxxx.puredaily.entity.daily;

import java.util.Arrays;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/18.
 * Description:
 */
public class NewsDetails {

    public String body;
    public String image_source;
    public String title;
    public String image;
    public String share_url;
    public String ga_prefix;
    public Section section;
    public String[] images;
    public int id;

    @Override
    public String toString() {
        return "NewsDetails{" +
                "body='" + body + '\'' +
                ", image_source='" + image_source + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", share_url='" + share_url + '\'' +
                ", ga_prefix='" + ga_prefix + '\'' +
                ", section=" + section +
                ", images=" + Arrays.toString(images) +
                ", id=" + id +
                '}';
    }

    public class Section {
        public String thumbnail;
        public int id;
        public String name;

        @Override
        public String toString() {
            return "Section{" +
                    "thumbnail='" + thumbnail + '\'' +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

}
