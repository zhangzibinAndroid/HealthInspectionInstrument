package com.returnlive.healthinspectioninstrument.fragment.menu;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.linktop.MonitorDataTransmissionManager;
import com.linktop.constant.BluetoothState;
import com.linktop.infs.OnDeviceVersionListener;
import com.linktop.infs.OnSPO2HResultListener;
import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.activity.DataDealActivity;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.bean.EventMessage;
import com.returnlive.healthinspectioninstrument.fragment.other.BlueDialogListFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/28 0028
 * 时间： 上午 11:55
 * 描述： 首页
 */
public class HomePageFragment extends BaseFragment implements OnDeviceVersionListener, OnSPO2HResultListener {
    private static final String TAG = "HomePageFragment";
    @BindView(R.id.tv_bluetooth_connect)
    TextView tvBluetoothConnect;
    private BlueDialogListFragment blueDialogListFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_page, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        EventBus.getDefault().register(this);
        manager = MonitorDataTransmissionManager.getInstance();
        if (BlueDialogListFragment.isBlueConnect){
            tvBluetoothConnect.setText("蓝牙已连接");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.lay_connect_bluetooth, R.id.lay_ecg, R.id.lay_blood_pressure, R.id.lay_blood_oxygen, R.id.lay_body_temperature})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lay_connect_bluetooth:
                if (!BlueDialogListFragment.isBlueConnect) {
                    showBlueDeceiveList();
                    manager.setOnDeviceVersionListener(this);
                } else {
                    manager.disConnectBle();
                    BlueDialogListFragment.isBlueConnect = false;
                    tvBluetoothConnect.setText("点此连接");
                    Toast.makeText(getActivity(), "蓝牙已断开", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.lay_ecg:
                if (BlueDialogListFragment.isBlueConnect) {
                    EventBus.getDefault().postSticky(new EventMessage("ecg"));
                    JumpActivity(DataDealActivity.class);
                } else {
                    Toast.makeText(getActivity(), "请先连接蓝牙", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lay_blood_pressure:
                if (BlueDialogListFragment.isBlueConnect) {
                    EventBus.getDefault().postSticky(new EventMessage("blood_pressure"));
                    JumpActivity(DataDealActivity.class);
                } else {
                    Toast.makeText(getActivity(), "请先连接蓝牙", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lay_blood_oxygen:
                if (BlueDialogListFragment.isBlueConnect) {
                    EventBus.getDefault().postSticky(new EventMessage("blood_oxygen"));
                    JumpActivity(DataDealActivity.class);
                } else {
                    Toast.makeText(getActivity(), "请先连接蓝牙", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lay_body_temperature:
                if (BlueDialogListFragment.isBlueConnect) {
                    EventBus.getDefault().postSticky(new EventMessage("body_temperature"));
                    JumpActivity(DataDealActivity.class);
                } else {
                    Toast.makeText(getActivity(), "请先连接蓝牙", Toast.LENGTH_SHORT).show();
                }
                break;

        }


    }

    private void showBlueDeceiveList() {
        blueDialogListFragment = new BlueDialogListFragment();
        blueDialogListFragment.show(getActivity().getFragmentManager(), "");
    }

    @Override
    public void onDeviceSoftwareVersion(String s) {
        runOnUiMothod(tvBluetoothConnect, "正在连接");
    }

    @Override
    public void onDeviceHardwareVersion(String s) {
        runOnUiMothod(tvBluetoothConnect, "蓝牙已连接");
        BlueDialogListFragment.isBlueConnect = true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMessage(EventMessage eventMessage) {
        switch (eventMessage.getMsg()) {
            case BluetoothState.BLE_CLOSED:
                tvBluetoothConnect.setText("蓝牙未开启");
                break;
            case BluetoothState.BLE_OPENED_AND_DISCONNECT:
                tvBluetoothConnect.setText("点此连接");
                break;
            case BluetoothState.BLE_CONNECTING_DEVICE:
                tvBluetoothConnect.setText("正在连接");
                break;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSPO2HResult(int i, int i1) {
        Log.e(TAG, "i: " + i);
        Log.e(TAG, "i1: " + i1);
    }

    @Override
    public void onSPO2HWave(int i) {
        Log.e(TAG, "onSPO2HWave: " + i);

    }
}
