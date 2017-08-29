package com.returnlive.healthinspectioninstrument.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.linktop.whealthService.HealthApi;
import com.linktop.whealthService.task.BT_task;
import com.linktop.whealthService.task.Bs_task;
import com.linktop.whealthService.task.battery_task;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Unbinder;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/11 0011
 * 时间： 下午 12:03
 * 描述： Fragment基类
 */

public class BaseFragment extends Fragment implements HealthApi.EcgCallBack, HealthApi.HealthApiCallBack, HealthApi.BleCallBack {
    protected View view;
    protected Unbinder unbinder;
    protected static final String TAG = "BaseFragment";
    protected Timer batteryQueryTimer;
    protected HealthApi mHealthApi;
    protected boolean bleOn = false;
    protected final boolean START = true;
    protected final boolean STOP = false;
    protected boolean bp_button_state = START;
    protected boolean bt_button_state = START;
    protected boolean ox_button_state = START;
    protected boolean ecg_button_state = START;
    protected boolean bs_button_state = START;
    protected boolean ecg_modual_exist = false;
    protected boolean bs_modual_exist;
    protected static final int REQUEST_BLUETOOTH_DEVICE = 0;
    protected static final int REQUEST_OPEN_BT = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        mHealthApi = new HealthApi();
        mHealthApi.init(getActivity(), this, mbpHandler, mbtHandler, moxHandler, mbsHandler, this);
        mHealthApi.setBleCallBack(this);
    }

    protected Handler mbtHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BT_task.BT_RESULT:
                    double result = msg.getData().getDouble("BodyTempResult");
                    tmepCallBack.getTmepCallBack(String.format("%.1f", result),result);
                    bt_button_state = START;
                    break;

                default:
                    break;
            }
        }

        ;
    };

    @SuppressLint("HandlerLeak")
    protected Handler moxHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            oxygenCallBack.getOxygenCall(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    protected Handler mbpHandler = new Handler() {
        public void handleMessage(Message msg) {
            bloodPressureDataCallBack.getBloodPressureDataCallBack(msg);
        }
    };
    @SuppressLint("HandlerLeak")
    protected Handler mbsHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Bs_task.BS_PAPER_STATUS:
                    int init_paper = msg.getData().getByte("initpaperStatus");
                    if (init_paper == Bs_task.PAPER_OUT) {
                        bs_button_state = START;
//                        bloodsugar.setText(R.string.startbs);
//                        bsValue.setText(R.string.paper_not_in);
                    } else if (init_paper == Bs_task.PAPER_IN)
                        // contentFragment.bsFragment.dialog();
//                        bsValue.setText(R.string.paper_in);
                        break;
                case Bs_task.BS_PAPER_IS_READY:
//                    bsValue.setText(R.string.blood_in);
                    break;
                case Bs_task.BS_PAPER_CHECK_TIMEOUT:
                    bs_button_state = START;
//                    bloodsugar.setText(R.string.startbs);
//                    bsValue.setText(R.string.paper_in_timeout);
                    break;
                case Bs_task.BS_BLOOD_CHECK_TIMEOUT:
                    bs_button_state = START;
//                    bloodsugar.setText(R.string.startbs);
//                    bsValue.setText(R.string.blood_in_timeout);
                    break;
                case Bs_task.BS_TESTING_PAPER_OUT:
                    bs_button_state = START;
//                    bloodsugar.setText(R.string.startbs);
//                    bsValue.setText(R.string.paper_out);
                    break;
                case Bs_task.BS_RESULT:
                    bs_button_state = START;
//                    bloodsugar.setText(R.string.startbs);
                    double data = msg.getData().getDouble("bsresult");
//                    bsValue.setText(String.format(getString(R.string.bsValue), ""
//                            + data));
                    break;
                case Bs_task.BS_PAPER_IS_USED:
                    bs_button_state = START;
//                    bloodsugar.setText(R.string.startbs);
//                    bsValue.setText(R.string.paper_used);
                    break;
                case Bs_task.BS_GET_VER_FAIL:
                    bs_button_state = START;
//                    bloodsugar.setText(R.string.startbs);
//                    bsValue.setText(R.string.getver_fail);
                    break;
                case Bs_task.BS_BLOOD_IN_DETECTED:
//                    bsValue.setText(R.string.blood_in_detected);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onReceive(int[] data) {
        ecgCallBack.ecgCall(data);

    }

    @Override
    public void healthCallBack(int type, Object data) {
        switch (type) {
            case HealthApi.BIND_FINISHED:

                break;
            case HealthApi.BATTERY:
                setBatteryUi((byte[]) data);
                break;
            case HealthApi.UPDATA_CON_INTERVAL_FAIL_ACTION:
                Toast.makeText(getActivity(), "update connect interval error", Toast.LENGTH_SHORT).show();
                break;
            case HealthApi.QUERY_ECG_MODULE_EXIST:
                Log.e(TAG, "ECG data" + data.toString());
                byte modual = (Byte) data;
                if (modual == 0)
                    ecg_modual_exist = false;
                else if (modual == 1) {
                    ecg_modual_exist = true;
                }
                break;
            case HealthApi.QUERY_BS_MODULE_EXIST:
                Log.e(TAG, "BS data" + data.toString());
                byte modual1 = (Byte) data;

                if (modual1 == 0)
                    bs_modual_exist = false;
                else if (modual1 == 1) {
                    bs_modual_exist = true;
                }
                break;
            case HealthApi.INTENT_DEVICE_VERSION_SOFE:
                String sofe = (String) data;
                break;
            case HealthApi.INTENT_DEVICE_VERSION_HARD:
                String hard = (String) data;
                break;
        }
    }


    private void setBatteryUi(byte[] battery) {

        switch (battery[0]) {
            case battery_task.BATTERY_QUERY:
                Toast.makeText(getActivity(), "设备电量剩余" + battery[1] + "%", Toast.LENGTH_SHORT).show();
                break;
            case battery_task.BATTERY_CHARING:
                Toast.makeText(getActivity(), "设备正在充电...", Toast.LENGTH_SHORT).show();
                break;
            case battery_task.BATTERY_FULL:
                Toast.makeText(getActivity(), "充电完成", Toast.LENGTH_SHORT).show();
            default:
                break;
        }
    }

    @Override
    public void bleCallBack(int type, Object data) {
        if (type == HealthApi.BLE_STATUS) {
            setBleUiState((Integer) data);
        }
    }

    private void setBleUiState(int mState) {
        bleUiStateCallBack.BleUiStateCall(mState);


    }


    protected void modual_init() {
        mHealthApi.ecgQuery();
        mHealthApi.bsQuery();
        mHealthApi.batteryQuery();
        if (batteryQueryTimer == null)
            batteryQueryTimer = new Timer();
        batteryQueryTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHealthApi.batteryQuery();
            }
        }, 300000, 300000);
    }


    private void disconect() {
        mHealthApi.bleDisconnect();
    }

    protected BleUiStateCallBack bleUiStateCallBack;

    protected static interface BleUiStateCallBack{
        void BleUiStateCall(int mState);
    }

    protected void setBleUiStateCallBack(BleUiStateCallBack bleUiStateCallBack){
        this.bleUiStateCallBack = bleUiStateCallBack;
    }

    protected void JumpActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }


    protected EcgCallBack ecgCallBack;
    protected static interface EcgCallBack{
        void ecgCall(int[] data);
    }

    protected void setEcgCallBack(EcgCallBack ecgCallBack){
        this.ecgCallBack = ecgCallBack;
    }

    protected TmepCallBack tmepCallBack;

    protected static interface TmepCallBack{
        void getTmepCallBack(String data,double temData);
    }

    protected void setTmepCallBack(TmepCallBack tmepCallBack){
        this.tmepCallBack = tmepCallBack;
    }


    protected OxygenCallBack oxygenCallBack;

    protected static interface OxygenCallBack{
        void getOxygenCall(Message msg);
    }

    protected void setOxygenCallBack(OxygenCallBack oxygenCallBack){
        this.oxygenCallBack = oxygenCallBack;
    }

    protected BloodPressureDataCallBack bloodPressureDataCallBack;

    protected static interface BloodPressureDataCallBack{
        void getBloodPressureDataCallBack(Message msg);
    }

    protected void setBloodPressureDataCallBack(BloodPressureDataCallBack bloodPressureDataCallBack){
        this.bloodPressureDataCallBack = bloodPressureDataCallBack;
    }
}
