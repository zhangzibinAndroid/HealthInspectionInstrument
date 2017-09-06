package com.returnlive.healthinspectioninstrument.fragment.measure;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnSPO2HResultListener;
import com.linktop.whealthService.MeasureType;
import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.fragment.other.BlueDialogListFragment;
import com.returnlive.healthinspectioninstrument.view.ProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private boolean isMeasureBo = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blood_oxygen_measure, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        manager = MonitorDataTransmissionManager.getInstance();
        tvHrWarning.setVisibility(View.INVISIBLE);
        isMeasureBo = false;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        manager.stopMeasure(MeasureType.SPO2H);
        btnStartMeasureOxygen.setText("开始");
        unbinder.unbind();
    }

    @OnClick(R.id.btn_start_measure_oxygen)
    public void onViewClicked() {
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
    }

    @Override
    public void onSPO2HResult(final int spo2h, final int heartRate) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                manager.stopMeasure(MeasureType.SPO2H);
                isMeasureBo = false;
                tvHrWarning.setVisibility(View.VISIBLE);
                btnStartMeasureOxygen.setText("开始");
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

                int angle = 96*320/110;
                progressViewOxygen.setAngleWithAnim(angle);

                if (heartRate < 60) {
                    tvHrWarning.setText("心率：" + spo2h + ",心率偏低");
                    tvHrWarning.setTextColor(getResources().getColor(R.color.progress_orange));
                } else if (heartRate > 100) {
                    tvHrWarning.setText("心率：" + spo2h + ",心率偏高");
                    tvHrWarning.setTextColor(getResources().getColor(R.color.progress_red));
                } else {
                    tvHrWarning.setText("心率：" + spo2h + ",心率正常");
                    tvHrWarning.setTextColor(getResources().getColor(R.color.progress_green));
                }
                long time = System.currentTimeMillis();
                dbManager.addBloodOxMessage(time+"",spo2h+"",heartRate+"");
            }
        });


    }

    @Override
    public void onSPO2HWave(int i) {
        //血氧测量画图线数据
        runOnUiMothod(tvOxygenWarning, "正在测量：" + i);
    }
}
