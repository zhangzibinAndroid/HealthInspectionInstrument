package com.returnlive.healthinspectioninstrument.bean.health_updata.bp;

import com.google.gson.annotations.SerializedName;
import com.returnlive.healthinspectioninstrument.bean.health_updata.ecg_bean.DetectItem;

import java.io.Serializable;

/**
 * 血压
 */
public class BloodPressureMeasuredData implements Serializable {

    @SerializedName("5-1")
    private DetectItem lowPressure;//低压

    @SerializedName("5-2")
    private DetectItem highPressure;//高压

    public DetectItem getLowPressure() {
        return lowPressure;
    }

    public void setLowPressure(DetectItem lowPressure) {
        this.lowPressure = lowPressure;
    }

    public DetectItem getHighPressure() {
        return highPressure;
    }

    public void setHighPressure(DetectItem highPressure) {
        this.highPressure = highPressure;
    }

}
