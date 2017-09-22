package com.returnlive.healthinspectioninstrument.bean.health_updata.bo;

import com.google.gson.annotations.SerializedName;
import com.returnlive.healthinspectioninstrument.bean.health_updata.ecg_bean.DetectItem;

import java.io.Serializable;

/**
 * 血氧
 */
public class OxygenMeasuredData implements Serializable {

    @SerializedName("8")
    private DetectItem oxygen;//血氧

    @SerializedName("9")
    private DetectItem heartRate;//心率

    public DetectItem getOxygen() {
        return oxygen;
    }

    public void setOxygen(DetectItem oxygen) {
        this.oxygen = oxygen;
    }

    public DetectItem getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(DetectItem heartRate) {
        this.heartRate = heartRate;
    }
}
