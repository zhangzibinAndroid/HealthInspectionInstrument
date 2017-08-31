package com.returnlive.healthinspectioninstrument.bean;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/31 0031
 * 时间： 上午 11:29
 * 描述： 血氧查询
 */

public class DbBloodOxBean {
    public String time;
    public String bloodOx;
    public String hr;

    public DbBloodOxBean() {
    }

    public DbBloodOxBean(String time, String bloodOx, String hr) {
        this.time = time;
        this.bloodOx = bloodOx;
        this.hr = hr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBloodOx() {
        return bloodOx;
    }

    public void setBloodOx(String bloodOx) {
        this.bloodOx = bloodOx;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }
}
