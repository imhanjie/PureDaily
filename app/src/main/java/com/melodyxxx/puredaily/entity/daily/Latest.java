package com.melodyxxx.puredaily.entity.daily;

import java.util.Arrays;
import java.util.List;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/14.
 * Description:
 */
public class Latest {

    public String date;
    public List<Story> stories;
    public List<TopStory> top_stories;

    @Override
    public String toString() {
        return "Latest{" +
                "date='" + date + '\'' +
                ", stories=" + stories +
                ", top_stories=" + top_stories +
                '}';
    }

    public class Story {
        // 可能为null
        public String[] images;
        public int type;
        public int id;
        public String ga_prefix;
        public String title;
        public boolean multipic;

        @Override
        public String toString() {
            return "Story{" +
                    "images=" + Arrays.toString(images) +
                    ", type=" + type +
                    ", id=" + id +
                    ", ga_prefix='" + ga_prefix + '\'' +
                    ", title='" + title + '\'' +
                    ", multipic=" + multipic +
                    '}';
        }
    }

    public class TopStory {

        public String image;
        public int type;
        public int id;
        public String ga_prefix;
        public String title;

        @Override
        public String toString() {
            return "TopStory{" +
                    "image='" + image + '\'' +
                    ", type=" + type +
                    ", id=" + id +
                    ", ga_prefix='" + ga_prefix + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }


}
