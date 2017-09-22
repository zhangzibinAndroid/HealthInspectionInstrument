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
import com.linktop.infs.OnBpResultListener;
import com.linktop.whealthService.MeasureType;
import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.bean.health_updata.bp.BloodPressureMeasuredData;
import com.returnlive.healthinspectioninstrument.bean.health_updata.ecg_bean.DetectItem;
import com.returnlive.healthinspectioninstrument.constant.Code;
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
 * 时间： 下午 4:21
 * 描述： 血压测量
 */
public class BloodPressureMeasureFragment extends BaseFragment implements OnBpResultListener {
    @BindView(R.id.progress_view_blood_pressure)
    ProgressView progressViewBloodPressure;
    @BindView(R.id.tv_blood_pressure_data)
    TextView tvBloodPressureData;
    @BindView(R.id.btn_start_measure_tem)
    Button btnStartMeasureTem;
    @BindView(R.id.tv_systolic_warning)
    TextView tvSystolicWarning;
    @BindView(R.id.tv_diastolic_warning)
    TextView tvDiastolicWarning;
    @BindView(R.id.btn_start_measure_save)
    Button btnStartMeasureSave;
    private boolean isMeasureBp = false;
    private int sys, dia;
    private BloodPressureMeasuredData bloodPressureMeasuredData;
    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blood_pressure_measure, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        gson = new Gson();
        bloodPressureMeasuredData = new BloodPressureMeasuredData();
        tvDiastolicWarning.setVisibility(View.INVISIBLE);
        btnStartMeasureSave.setVisibility(View.GONE);
        manager = MonitorDataTransmissionManager.getInstance();
        isMeasureBp = false;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        manager.stopMeasure(MeasureType.BP);
        btnStartMeasureTem.setText("开始");
        isMeasureBp = false;
        unbinder.unbind();
    }


    @Override
    public void onBpResult(final int systolicPressure, final int diastolicPressure, int heartRate) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sys = systolicPressure;
                dia = diastolicPressure;
                //高压
                DetectItem highPressureItem = new DetectItem();
                highPressureItem.setValue(Float.valueOf(sys));
                highPressureItem.setUnit("mmHg");
                bloodPressureMeasuredData.setHighPressure(highPressureItem);

                //低压
                DetectItem lowPressureItem = new DetectItem();
                lowPressureItem.setValue(Float.valueOf(dia));
                lowPressureItem.setUnit("mmHg");
                bloodPressureMeasuredData.setLowPressure(lowPressureItem);
                btnStartMeasureSave.setVisibility(View.VISIBLE);
                tvDiastolicWarning.setVisibility(View.VISIBLE);
                manager.stopMeasure(MeasureType.BP);
                isMeasureBp = false;
                btnStartMeasureTem.setText("开始");
                tvBloodPressureData.setText(systolicPressure + "/" + diastolicPressure);
                if (systolicPressure < 90) {
                    tvSystolicWarning.setText("收缩压：" + systolicPressure + ",收缩压偏低");
                    tvSystolicWarning.setTextColor(getResources().getColor(R.color.progress_orange));
                    progressViewBloodPressure.setColor(getResources().getColor(R.color.progress_orange));
                } else if (systolicPressure > 139) {
                    tvSystolicWarning.setText("收缩压：" + systolicPressure + ",收缩压偏高");
                    tvSystolicWarning.setTextColor(getResources().getColor(R.color.progress_red));
                    progressViewBloodPressure.setColor(getResources().getColor(R.color.progress_red));
                } else {
                    tvSystolicWarning.setText("收缩压：" + systolicPressure + ",收缩压正常");
                    tvSystolicWarning.setTextColor(getResources().getColor(R.color.progress_green));
                    progressViewBloodPressure.setColor(getResources().getColor(R.color.progress_green));
                }

                int angle = 100 * 320 / 150;
                progressViewBloodPressure.setAngleWithAnim(angle);
                if (diastolicPressure < 90) {
                    tvDiastolicWarning.setText("舒张压：" + diastolicPressure + ",舒张压偏低");
                    tvDiastolicWarning.setTextColor(getResources().getColor(R.color.progress_orange));
                } else if (diastolicPressure > 139) {
                    tvDiastolicWarning.setText("舒张压：" + diastolicPressure + ",舒张压偏高");
                    tvDiastolicWarning.setTextColor(getResources().getColor(R.color.progress_red));
                } else {
                    tvDiastolicWarning.setText("舒张压：" + diastolicPressure + ",舒张压正常");
                    tvDiastolicWarning.setTextColor(getResources().getColor(R.color.progress_green));
                }

            }
        });

    }

    @Override
    public void onLeakError(int i) {
        final int text = i == 0 ? R.string.leak_and_check : R.string.measurement_void;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                manager.stopMeasure(MeasureType.BP);
                btnStartMeasureTem.setText("开始");
                tvSystolicWarning.setText(getString(text));
                isMeasureBp = false;

            }
        });
    }

    @OnClick({R.id.btn_start_measure_tem, R.id.btn_start_measure_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_measure_tem:
                btnStartMeasureSave.setVisibility(View.GONE);
                if (!isMeasureBp) {
                    MonitorDataTransmissionManager.getInstance().startMeasure(MeasureType.BP);
                    MonitorDataTransmissionManager.getInstance().setOnBpResultListener(this);
                    btnStartMeasureTem.setText("停止");
                    tvDiastolicWarning.setVisibility(View.INVISIBLE);
                    isMeasureBp = true;
                } else {
                    manager.stopMeasure(MeasureType.BP);
                    btnStartMeasureTem.setText("开始");
                    isMeasureBp = false;
                }
                break;
            case R.id.btn_start_measure_save:
                showIOSLodingDialog("正在保存...");

                try {
                    long time = System.currentTimeMillis();
                    dbManager.addBloodPreMessage(time + "", sys + "", "" + dia);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            bpInterface();
                        }
                    }).start();

                } catch (Exception e) {
                    dismissIOSDialog();
                    Toast.makeText(getActivity(), "保存异常" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void bpInterface() {
        OkHttpUtils.post().url(UP_DATA_HEALTH + midUrl)
                .addParams("data", gson.toJson(bloodPressureMeasuredData))
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
                        bpHandler.sendMessage(msg);
                    }
                });
    }

    private Handler bpHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(Code.SUCCESS) > 0) {
                dismissIOSDialog();
                btnStartMeasureTem.setText("开始");
                btnStartMeasureSave.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getResources().getString(R.string.save_success), Toast.LENGTH_SHORT).show();
            }else {
                dismissIOSDialog();
                try {
                    returnCode = GsonParsing.sendCodeError(result);
                } catch (Exception e) {
                    Log.e(TAG, "血压异常: " + e.getMessage());
                }
                getCodeStatus(returnCode.getCode());
            }
        }
    };
}
