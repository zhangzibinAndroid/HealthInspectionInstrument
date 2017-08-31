package com.returnlive.healthinspectioninstrument.bean;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/31 0031
 * 时间： 下午 12:23
 * 描述： 血压查询
 */

public class DbBloodPreBean {
    public String time;
    public String bloodPreSys;
    public String bloodPreDia;

    public DbBloodPreBean() {
    }

    public DbBloodPreBean(String time, String bloodPreSys, String bloodPreDia) {
        this.time = time;
        this.bloodPreSys = bloodPreSys;
        this.bloodPreDia = bloodPreDia;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBloodPreSys() {
        return bloodPreSys;
    }

    public void setBloodPreSys(String bloodPreSys) {
        this.bloodPreSys = bloodPreSys;
    }

    public String getBloodPreDia() {
        return bloodPreDia;
    }

    public void setBloodPreDia(String bloodPreDia) {
        this.bloodPreDia = bloodPreDia;
    }
}
