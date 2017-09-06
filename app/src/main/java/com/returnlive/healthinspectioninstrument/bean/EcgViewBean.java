package com.returnlive.healthinspectioninstrument.bean;

import com.returnlive.healthinspectioninstrument.view.EcgPathViewHistory;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/31 0031
 * 时间： 下午 2:33
 * 描述： 心电
 */

public class EcgViewBean {
    EcgPathViewHistory ecgPathViewHistory;

    public EcgViewBean(EcgPathViewHistory ecgPathViewHistory) {
        this.ecgPathViewHistory = ecgPathViewHistory;
    }

    public EcgPathViewHistory getEcgPathViewHistory() {
        return ecgPathViewHistory;
    }

    public void setEcgPathViewHistory(EcgPathViewHistory ecgPathViewHistory) {
        this.ecgPathViewHistory = ecgPathViewHistory;
    }
}
