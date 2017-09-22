package com.returnlive.healthinspectioninstrument.application;

import android.app.Activity;
import android.app.Application;

import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/11 0011
 * 时间： 下午 3:12
 * 描述： Application
 */

public class HealthApplication extends Application {
    private static List<Activity> lists = new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
        //是否打开日志，true 打开，false关闭，默认打开
//        MonitorDataTransmissionManager.isDebug(true);
    }


    public static void addActivity(Activity activity) {
        lists.add(activity);
    }

    public static void clearActivity() {
        if (lists != null) {
            for (Activity activity : lists) {
                activity.finish();
            }

            lists.clear();
        }
    }
}
