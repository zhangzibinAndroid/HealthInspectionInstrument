package com.returnlive.healthinspectioninstrument.bean.health_updata.tem;

import com.google.gson.annotations.SerializedName;
import com.returnlive.healthinspectioninstrument.bean.health_updata.ecg_bean.DetectItem;

import java.io.Serializable;

/**
 * 体温
 */
public class TemperatureMeasuredData implements Serializable {

    @SerializedName("6")
    private DetectItem temperature;//体温

    public DetectItem getTemperature() {
        return temperature;
    }

    public void setTemperature(DetectItem temperature) {
        this.temperature = temperature;
    }
}
