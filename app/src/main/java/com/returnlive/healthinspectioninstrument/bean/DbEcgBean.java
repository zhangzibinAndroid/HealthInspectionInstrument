package com.returnlive.healthinspectioninstrument.bean;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/30 0030
 * 时间： 下午 4:07
 * 描述： 心电查询
 */

public class DbEcgBean {
    public String time;
    public String jsonData;
    public String hr;
    public String rr_max;
    public String rr_min;
    public String hrv;
    public String moods;
    public String brs;

    public DbEcgBean() {
    }

    public DbEcgBean(String time, String jsonData, String hr, String rr_max, String rr_min, String hrv, String moods, String brs) {
        this.time = time;
        this.jsonData = jsonData;
        this.hr = hr;
        this.rr_max = rr_max;
        this.rr_min = rr_min;
        this.hrv = hrv;
        this.moods = moods;
        this.brs = brs;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public String getRr_max() {
        return rr_max;
    }

    public void setRr_max(String rr_max) {
        this.rr_max = rr_max;
    }

    public String getRr_min() {
        return rr_min;
    }

    public void setRr_min(String rr_min) {
        this.rr_min = rr_min;
    }

    public String getHrv() {
        return hrv;
    }

    public void setHrv(String hrv) {
        this.hrv = hrv;
    }

    public String getMoods() {
        return moods;
    }

    public void setMoods(String moods) {
        this.moods = moods;
    }

    public String getBrs() {
        return brs;
    }

    public void setBrs(String brs) {
        this.brs = brs;
    }
}
