package com.returnlive.healthinspectioninstrument.constant;

/**
 * Created by 张梓彬 on 2017/9/22 0022.
 */

public class Code {
    public static final String SUCCESS = "success";
    public static final int PHONE_CODE = -11305;//请输入正确电话
    public static final int PHONE_HAVE = -11303;//电话以存在请直接登录
    public static final int PHONE_ERROR = -11601;//短信码获取失败
    public static final int SENDCODE_ERROR = -11306;//短信码获取错误
    public static final int USER_NOTEXIT = -11307;//用户不存在
    public static final int MID_NOTEXIT = -11201;//m_id不可为空
    public static final int UID_NOTEXIT = -11202;//uid不可为空
    public static final int DATA_EMPTY = -11701;//数据不可为空
    public static final int DATA_UPLOADING_FAILURE = -11103;//数据上传失败

}
