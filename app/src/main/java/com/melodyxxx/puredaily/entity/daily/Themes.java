package com.melodyxxx.puredaily.entity.daily;

import java.util.List;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/22.
 * Description:
 */

public class Themes {

    public int limit;
    public List<Theme> others;

    @Override
    public String toString() {
        return "Themes{" +
                "limit=" + limit +
                ", others=" + others +
                '}';
    }

}
