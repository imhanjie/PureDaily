package com.melodyxxx.puredaily.entity.app;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/18.
 * Description:
 */
public class LatestVersion {

    public String version_name;
    public int version_code;
    public String update_time;
    public String changelog;
    public String download_url;

    @Override
    public String toString() {
        return "LatestVersion{" +
                "version_name='" + version_name + '\'' +
                ", version_code=" + version_code +
                ", update_time='" + update_time + '\'' +
                ", changelog='" + changelog + '\'' +
                ", download_url='" + download_url + '\'' +
                '}';
    }
}
