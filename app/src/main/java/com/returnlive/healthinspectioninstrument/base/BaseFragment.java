package com.returnlive.healthinspectioninstrument.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.linktop.MonitorDataTransmissionManager;
import com.returnlive.healthinspectioninstrument.db.DBManager;

import butterknife.Unbinder;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/11 0011
 * 时间： 下午 12:03
 * 描述： Fragment基类
 */

public class BaseFragment extends Fragment{
    protected View view;
    protected Unbinder unbinder;
    protected static final String TAG = "BaseFragment";
    protected DBManager dbManager;
    protected Gson gson;
    protected MonitorDataTransmissionManager manager;


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


    protected void runOnUiMothod(final TextView textView, final String data){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(data);
            }
        });
    }
}
