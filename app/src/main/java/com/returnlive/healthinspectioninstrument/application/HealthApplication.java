package com.returnlive.healthinspectioninstrument.application;

import android.app.Application;

import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/11 0011
 * 时间： 下午 3:12
 * 描述： Application
 */

public class HealthApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
    }
}
