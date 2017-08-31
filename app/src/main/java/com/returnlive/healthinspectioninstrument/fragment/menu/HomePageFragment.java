package com.returnlive.healthinspectioninstrument.fragment.menu;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.linktop.whealthService.OnBLEService;
import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.activity.DataDealActivity;
import com.returnlive.healthinspectioninstrument.activity.DeviceList;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.bean.EventMessage;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/28 0028
 * 时间： 上午 11:55
 * 描述： 首页
 */
public class HomePageFragment extends BaseFragment {
    private static final String TAG = "HomePageFragment";
    @BindView(R.id.tv_bluetooth_connect)
    TextView tvBluetoothConnect;

    private static boolean isConnect = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_page, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        setBleUiStateCallBack(bleUiStateCallBack);
        if (bleOn) {
            tvBluetoothConnect.setText("蓝牙已连接");
        } else {
            tvBluetoothConnect.setText(getActivity().getResources().getString(R.string.click_connect));
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
                if (BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    if (!bleOn) {// mState != BLE_DISCONNECTED
                        startActivityForResult(new Intent(getActivity(), DeviceList.class), REQUEST_BLUETOOTH_DEVICE);
                    } else {
                        mHealthApi.bleDisconnect();
                    }
                } else {
                    enableBluetooth();
                }

                break;
            case R.id.lay_ecg:
                if (isConnect) {
                    EventBus.getDefault().postSticky(new EventMessage("ecg"));
                    JumpActivity(DataDealActivity.class);
                } else {
                    Toast.makeText(getActivity(), "请先连接蓝牙", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lay_blood_pressure:
                if (isConnect) {
                    EventBus.getDefault().postSticky(new EventMessage("blood_pressure"));
                    JumpActivity(DataDealActivity.class);
                } else {
                    Toast.makeText(getActivity(), "请先连接蓝牙", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lay_blood_oxygen:
                if (isConnect) {
                    EventBus.getDefault().postSticky(new EventMessage("blood_oxygen"));
                    JumpActivity(DataDealActivity.class);
                } else {
                    Toast.makeText(getActivity(), "请先连接蓝牙", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lay_body_temperature:
                if (isConnect) {
                    EventBus.getDefault().postSticky(new EventMessage("body_temperature"));
                    JumpActivity(DataDealActivity.class);
                } else {
                    Toast.makeText(getActivity(), "请先连接蓝牙", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    private void enableBluetooth() {
        Log.e(TAG, "enableBluetooth: ");
        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
        if (bta == null) {
            Toast.makeText(getActivity(), "不支持蓝牙4.0", Toast.LENGTH_SHORT)
                    .show();
        } else {
            if (!bta.isEnabled()) {
                startActivityForResult(new Intent(
                                "android.bluetooth.adapter.action.REQUEST_ENABLE"),
                        REQUEST_OPEN_BT);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_BLUETOOTH_DEVICE:
                if (resultCode == RESULT_OK) {
                    String strAddress = data.getExtras().getString(
                            BluetoothDevice.EXTRA_DEVICE);
                    if (!mHealthApi.bleConnect(strAddress)) {
                        Log.e(TAG, "连接失败");
                    }
                } else {
                }
                break;

            case REQUEST_OPEN_BT:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getActivity(), "蓝牙已开启", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "打开蓝牙失败", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    private BleUiStateCallBack bleUiStateCallBack = new BleUiStateCallBack() {
        @Override
        public void BleUiStateCall(int mState) {
            switch (mState) {
                case OnBLEService.BLE_CONNECTED:
                    bleOn = true;
                    tvBluetoothConnect.setText("正在连接");
                    isConnect = false;
                    break;
                case OnBLEService.BLE_DISCONNECTED:
                    bleOn = false;
                    tvBluetoothConnect.setText(getActivity().getResources().getString(R.string.click_connect));
                    Toast.makeText(getActivity(), "蓝牙已断开", Toast.LENGTH_SHORT).show();
                    isConnect = false;
                    break;
                case OnBLEService.BLE_NOTIFICATION_ENABLED:
                    tvBluetoothConnect.setText("蓝牙已连接");
                    Toast.makeText(getActivity(), "蓝牙已连接", Toast.LENGTH_SHORT).show();
                    isConnect = true;
                    modual_init();
                    break;
                case OnBLEService.BLE_NOTIFICATION_NOTENABLED:
                    tvBluetoothConnect.setText("蓝牙连接失败");
                    isConnect = false;
                    break;
                default:
                    break;
            }
        }
    };
}
