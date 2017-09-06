package com.returnlive.healthinspectioninstrument.bean;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/28 0028
 * 时间： 下午 5:09
 * 描述： EventBus消息类
 */

public class EventMessage {
    public String message;
    public int msg;

    public EventMessage(int msg) {
        this.msg = msg;
    }

    public EventMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getMsg() {
        return msg;
    }
}
