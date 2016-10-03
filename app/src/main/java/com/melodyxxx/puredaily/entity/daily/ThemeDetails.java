package com.melodyxxx.puredaily.entity.daily;

import java.util.Arrays;
import java.util.List;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/10/01.
 * Description:
 */

public class ThemeDetails {

    public List<Story> stories;
    public String description;
    public String background;
    public String name;

    @Override
    public String toString() {
        return "ThemeDetails{" +
                "stories=" + stories +
                ", description='" + description + '\'' +
                ", background='" + background + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public class Story {
        public String[] images;
        public int type;
        public int id;
        public String title;

        @Override
        public String toString() {
            return "Story{" +
                    "images=" + Arrays.toString(images) +
                    ", type=" + type +
                    ", id=" + id +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

}
