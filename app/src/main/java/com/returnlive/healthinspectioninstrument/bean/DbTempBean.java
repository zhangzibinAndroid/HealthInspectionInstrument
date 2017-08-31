package com.returnlive.healthinspectioninstrument.bean;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/31 0031
 * 时间： 上午 10:40
 * 描述： 体温查询
 */

public class DbTempBean {
    public String time;
    public String data;

    public DbTempBean() {
    }

    public DbTempBean(String time, String data) {
        this.time = time;
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
