package com.returnlive.healthinspectioninstrument.bean;

/**
 * 作者： 张梓彬
 * 日期： 2017/9/22 0022
 * 时间： 上午 10:53
 * 描述： 返回码
 */

public class ReturnCode {

    /**
     * status : error
     * code : -11303
     */

    private String status;
    private int code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
