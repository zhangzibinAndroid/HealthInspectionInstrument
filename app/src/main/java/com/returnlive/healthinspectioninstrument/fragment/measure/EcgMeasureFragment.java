package com.returnlive.healthinspectioninstrument.fragment.measure;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.linktop.whealthService.HealthApi;
import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.view.EcgPathView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/28 0028
 * 时间： 下午 5:54
 * 描述： 心电测量
 */
public class EcgMeasureFragment extends BaseFragment {

    @BindView(R.id.tv_rr_max)
    TextView tvRrMax;
    @BindView(R.id.tv_rr_min)
    TextView tvRrMin;
    @BindView(R.id.tv_mood)
    TextView tvMood;
    @BindView(R.id.tv_heart_rate)
    TextView tvHeartRate;
    @BindView(R.id.tv_heart_rate_variability)
    TextView tvHeartRateVariability;
    @BindView(R.id.tv_breathing_rate)
    TextView tvBreathingRate;
    @BindView(R.id.btn_start_measure)
    Button btnStartMeasure;
    @BindView(R.id.ecg_view)
    EcgPathView ecgView;

    public float mWidth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ecg_measure, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        setEcgCallBack(ecgCallBack);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (ecg_button_state == STOP) {
            mHealthApi.ecgStop();
            ecg_button_state = START;
            btnStartMeasure.setText("开始");
        }
        unbinder.unbind();

    }

    @OnClick(R.id.btn_start_measure)
    public void onViewClicked() {
        if (ecg_button_state == START) {
            ecgView.getArrast().clear();
            mHealthApi.ecgStart();
            ecg_button_state = STOP;
            btnStartMeasure.setText("停止");
            tvHeartRate.setText("心率：");
            tvRrMax.setText("RR最大值：");
            tvRrMin.setText("RR最小值：");
            tvHeartRateVariability.setText("心率变异性：");
            tvMood.setText("心情：");
            tvBreathingRate.setText("呼吸率：");
        } else {
            mHealthApi.ecgStop();
            ecg_button_state = START;
            btnStartMeasure.setText("开始");
        }
    }


    private EcgCallBack ecgCallBack = new EcgCallBack() {
        @Override
        public void ecgCall(int[] data) {
            switch (data[0]) {
                case HealthApi.ECG_DATA:
                    if (ecgView.getArrast().size() != 0) {
                        mWidth = (float) (mWidth + 0.5);
                    } else {
                        mWidth = 0;
                    }
                    ecgView.setWidthes(mWidth);
                    ecgView.addDATA(data[1]);

                    break;
                case HealthApi.ECG_HEARTRATE:
                    final int avg_hr = (Integer) data[1];
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            tvHeartRate.setText("心率：" + avg_hr);
                        }
                    });
                    break;
                case HealthApi.ECG_RRMAX:
                    final int rrmaxint = data[1];
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvRrMax.setText("RR最大值：" + rrmaxint);
                        }
                    });
                    break;
                case HealthApi.ECG_RRMIN:
                    final int rrminint = data[1];
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvRrMin.setText("RR最小值：" + rrminint);
                        }
                    });
                    break;
                case HealthApi.ECG_HRV:
                    final int hRV = data[1];
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvHeartRateVariability.setText("心率变异性：" + hRV);
                        }
                    });
                    break;
                case HealthApi.ECG_MOOD:
                    final int mood = data[1];
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvMood.setText("心情：" + mood);
                        }
                    });
                    break;
                case HealthApi.ECG_BR:
                    final int br = data[1];
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            tvBreathingRate.setText("呼吸率：" + br);
                        }
                    });
                    break;
                case HealthApi.ECG_END:
                    ecg_button_state = START;
                    final long timeData = System.currentTimeMillis();
                    String time = timeData + "";
                    String jsonData = gson.toJson(ecgView.getArrast());
                    String hr = tvHeartRate.getText().toString();
                    String rr_max = tvRrMax.getText().toString();
                    String rr_min = tvRrMin.getText().toString();
                    String hrv = tvHeartRateVariability.getText().toString();
                    String moods = tvMood.getText().toString();
                    String brs = tvBreathingRate.getText().toString();
                    dbManager.addEcgMessage(time, jsonData, hr, rr_max, rr_min, hrv, moods, brs);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnStartMeasure.setText("开始");
                        }
                    });
                    break;

                default:
                    break;
            }
        }
    };

}
