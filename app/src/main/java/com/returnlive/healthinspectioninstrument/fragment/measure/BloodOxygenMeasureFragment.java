package com.returnlive.healthinspectioninstrument.fragment.measure;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnSPO2HResultListener;
import com.linktop.whealthService.MeasureType;
import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.bean.health_updata.bo.OxygenMeasuredData;
import com.returnlive.healthinspectioninstrument.bean.health_updata.ecg_bean.DetectItem;
import com.returnlive.healthinspectioninstrument.constant.Code;
import com.returnlive.healthinspectioninstrument.fragment.other.BlueDialogListFragment;
import com.returnlive.healthinspectioninstrument.gson.GsonParsing;
import com.returnlive.healthinspectioninstrument.view.ProgressView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.returnlive.healthinspectioninstrument.constant.InstanceUrl.UP_DATA_HEALTH;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/29 0029
 * 时间： 下午 3:04
 * 描述： 血氧测量
 */
public class BloodOxygenMeasureFragment extends BaseFragment implements OnSPO2HResultListener {


    @BindView(R.id.progress_view_oxygen)
    ProgressView progressViewOxygen;
    @BindView(R.id.tv_oxygen_data)
    TextView tvOxygenData;
    @BindView(R.id.btn_start_measure_oxygen)
    Button btnStartMeasureOxygen;
    @BindView(R.id.tv_oxygen_warning)
    TextView tvOxygenWarning;
    @BindView(R.id.tv_hr_warning)
    TextView tvHrWarning;
    @BindView(R.id.btn_save)
    Button btnSave;
    private boolean isMeasureBo = false;
    private int spo, hr;
    private OxygenMeasuredData oxygenMeasuredData;
    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blood_oxygen_measure, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        gson = new Gson();
        oxygenMeasuredData = new OxygenMeasuredData();
        manager = MonitorDataTransmissionManager.getInstance();
        tvHrWarning.setVisibility(View.INVISIBLE);
        btnSave.setVisibility(View.GONE);
        isMeasureBo = false;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        manager.stopMeasure(MeasureType.SPO2H);
        btnStartMeasureOxygen.setText("开始");
        unbinder.unbind();
    }


    @Override
    public void onSPO2HResult(final int spo2h, final int heartRate) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spo = spo2h;
                hr = heartRate;

                //血氧
                DetectItem oxygenItem = new DetectItem();
                oxygenItem.setValue(Float.valueOf(spo));
                oxygenItem.setUnit("");
                oxygenMeasuredData.setOxygen(oxygenItem);

                //心率
                DetectItem heartRateItem = new DetectItem();
                heartRateItem.setValue(Float.valueOf(hr));
                heartRateItem.setUnit("");
                oxygenMeasuredData.setHeartRate(heartRateItem);

                manager.stopMeasure(MeasureType.SPO2H);
                isMeasureBo = false;
                btnSave.setVisibility(View.VISIBLE);
                tvHrWarning.setVisibility(View.VISIBLE);
                btnStartMeasureOxygen.setText("重新开始");
                tvOxygenData.setText(spo2h + "/" + heartRate);
                if (spo2h < 95) {
                    tvOxygenWarning.setText("血氧：" + spo2h + ",血氧偏低");
                    tvOxygenWarning.setTextColor(getResources().getColor(R.color.progress_orange));
                    progressViewOxygen.setColor(getResources().getColor(R.color.progress_orange));
                } else if (spo2h > 99) {
                    tvOxygenWarning.setText("血氧：" + spo2h + ",血氧偏高");
                    tvOxygenWarning.setTextColor(getResources().getColor(R.color.progress_red));
                    progressViewOxygen.setColor(getResources().getColor(R.color.progress_red));
                } else {
                    tvOxygenWarning.setText("血氧：" + spo2h + ",血氧正常");
                    tvOxygenWarning.setTextColor(getResources().getColor(R.color.progress_green));
                    progressViewOxygen.setColor(getResources().getColor(R.color.progress_green));
                }

                int angle = 96 * 320 / 110;
                progressViewOxygen.setAngleWithAnim(angle);

                if (heartRate < 60) {
                    tvHrWarning.setText("心率：" + heartRate + ",心率偏低");
                    tvHrWarning.setTextColor(getResources().getColor(R.color.progress_orange));
                } else if (heartRate > 100) {
                    tvHrWarning.setText("心率：" + heartRate + ",心率偏高");
                    tvHrWarning.setTextColor(getResources().getColor(R.color.progress_red));
                } else {
                    tvHrWarning.setText("心率：" + heartRate + ",心率正常");
                    tvHrWarning.setTextColor(getResources().getColor(R.color.progress_green));
                }

            }
        });


    }

    @Override
    public void onSPO2HWave(int i) {
        //血氧测量画图线数据
        runOnUiMothod(tvOxygenWarning, "正在测量：" + i);
    }

    @OnClick({R.id.btn_start_measure_oxygen, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_measure_oxygen:
                btnSave.setVisibility(View.GONE);
                if (BlueDialogListFragment.isBlueConnect) {
                    if (!isMeasureBo) {
                        manager.startMeasure(MeasureType.SPO2H);
                        manager.setOnSPO2HResultListener(this);
                        tvHrWarning.setVisibility(View.INVISIBLE);
                        btnStartMeasureOxygen.setText("停止");
                        isMeasureBo = true;
                    } else {
                        manager.stopMeasure(MeasureType.SPO2H);
                        btnStartMeasureOxygen.setText("开始");
                        isMeasureBo = false;
                    }
                } else {
                    Toast.makeText(getActivity(), "蓝牙未连接", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_save:
                try {
                    long time = System.currentTimeMillis();
                    dbManager.addBloodOxMessage(time + "", spo + "", hr + "");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boInterface();
                        }
                    }).start();
                } catch (Exception e) {
                    dismissIOSDialog();
                    Toast.makeText(getActivity(), "保存异常" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void boInterface() {
        OkHttpUtils.post().url(UP_DATA_HEALTH + midUrl)
                .addParams("data", gson.toJson(oxygenMeasuredData))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dismissIOSDialog();
                                Toast.makeText(getActivity(), getResources().getString(R.string.net_error), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Message msg = new Message();
                        msg.obj = response;
                        boHandler.sendMessage(msg);
                    }
                });
    }


    private Handler boHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(Code.SUCCESS) > 0) {
                dismissIOSDialog();
                btnStartMeasureOxygen.setText("开始");
                btnSave.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getResources().getString(R.string.save_success), Toast.LENGTH_SHORT).show();
            }else {
                dismissIOSDialog();
                try {
                    returnCode = GsonParsing.sendCodeError(result);
                } catch (Exception e) {
                    Log.e(TAG, "血氧异常: " + e.getMessage());
                }
                getCodeStatus(returnCode.getCode());
            }

        }
    };
}
