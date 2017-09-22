package com.returnlive.healthinspectioninstrument.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.linktop.MonitorDataTransmissionManager;
import com.returnlive.dialoglibrary.IOSLoadingDialog;
import com.returnlive.healthinspectioninstrument.bean.ReturnCode;
import com.returnlive.healthinspectioninstrument.db.DBManager;

import butterknife.Unbinder;

import static com.returnlive.healthinspectioninstrument.constant.Code.DATA_EMPTY;
import static com.returnlive.healthinspectioninstrument.constant.Code.DATA_UPLOADING_FAILURE;
import static com.returnlive.healthinspectioninstrument.constant.Code.MID_NOTEXIT;
import static com.returnlive.healthinspectioninstrument.constant.Code.PHONE_CODE;
import static com.returnlive.healthinspectioninstrument.constant.Code.PHONE_ERROR;
import static com.returnlive.healthinspectioninstrument.constant.Code.PHONE_HAVE;
import static com.returnlive.healthinspectioninstrument.constant.Code.SENDCODE_ERROR;
import static com.returnlive.healthinspectioninstrument.constant.Code.UID_NOTEXIT;
import static com.returnlive.healthinspectioninstrument.constant.Code.USER_NOTEXIT;
import static com.returnlive.healthinspectioninstrument.constant.InstanceUrl.mid;
import static com.returnlive.healthinspectioninstrument.constant.InstanceUrl.uid;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/11 0011
 * 时间： 下午 12:03
 * 描述： Fragment基类
 */

public class BaseFragment extends Fragment {
    protected View view;
    protected Unbinder unbinder;
    protected static final String TAG = "BaseFragment";
    protected DBManager dbManager;
    protected Gson gson;
    protected MonitorDataTransmissionManager manager;
    protected String midUrl = "/uid/" + uid + "/m_id/" + mid;
    protected IOSLoadingDialog iosLoadingDialog;
    protected ReturnCode returnCode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        dbManager = new DBManager(getActivity());
        gson = new Gson();
    }


    protected void JumpActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }


    protected void runOnUiMothod(final TextView textView, final String data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(data);
            }
        });
    }


    protected void runOnUiToast(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    protected void getCodeStatus(int code) {
        switch (code) {
            case PHONE_CODE:
                runOnUiToast("请输入正确电话");
                break;
            case PHONE_HAVE:
                runOnUiToast("电话以存在请直接登录");
                break;
            case PHONE_ERROR:
                runOnUiToast("短信码获取失败");
                break;
            case SENDCODE_ERROR:
                runOnUiToast("短信码错误");
                break;
            case USER_NOTEXIT:
                runOnUiToast("用户不存在");
                break;
            case MID_NOTEXIT:
                runOnUiToast("m_id不可为空");
                break;
            case UID_NOTEXIT:
                runOnUiToast("uid不可为空");
                break;
            case DATA_EMPTY:
                runOnUiToast("数据不可为空");
                break;
            case DATA_UPLOADING_FAILURE:
                runOnUiToast("数据上传失败");
                break;
        }
    }


    protected void showIOSLodingDialog(String hintMsg){
        iosLoadingDialog = new IOSLoadingDialog().setOnTouchOutside(true);
        iosLoadingDialog.setOnTouchOutside(false);
        iosLoadingDialog.setHintMsg(hintMsg);
        iosLoadingDialog.show(getActivity().getFragmentManager(), "iosLoadingDialog");
    }

    protected void dismissIOSDialog(){
        if (iosLoadingDialog!=null){
            iosLoadingDialog.dismiss();
        }
    }


}
