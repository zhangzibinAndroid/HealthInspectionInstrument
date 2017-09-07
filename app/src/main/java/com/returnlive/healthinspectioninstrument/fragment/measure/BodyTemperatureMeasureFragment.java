package com.returnlive.healthinspectioninstrument.fragment.measure;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linktop.MonitorDataTransmissionManager;
import com.linktop.infs.OnBtResultListener;
import com.linktop.whealthService.MeasureType;
import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.view.ProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/29 0029
 * 时间： 上午 10:37
 * 描述： 体温测量
 */
public class BodyTemperatureMeasureFragment extends BaseFragment implements OnBtResultListener {


    @BindView(R.id.tv_tem_data)
    TextView tvTemData;
    @BindView(R.id.btn_start_measure_tem)
    Button btnStartMeasureTem;
    @BindView(R.id.progress_view)
    ProgressView progressView;
    @BindView(R.id.tv_tem_warning)
    TextView tvTemWarning;
    @BindView(R.id.btn_start_measure_save)
    Button btnStartMeasureSave;
    private boolean isMeasureTemp = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_body_temperature_measure, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        isMeasureTemp = false;
        manager = MonitorDataTransmissionManager.getInstance();
        btnStartMeasureSave.setVisibility(View.GONE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isMeasureTemp = false;
        unbinder.unbind();
    }


    @Override
    public void onBtResult(final double temData) {
        isMeasureTemp = false;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnStartMeasureSave.setVisibility(View.VISIBLE);
                tvTemData.setText(temData + "");

                manager.resetMeasureFlag();
                btnStartMeasureTem.setText("重新开始");
                if (temData < 36) {
                    tvTemWarning.setText("体温：" + temData + ",体温偏低");
                    tvTemWarning.setTextColor(getResources().getColor(R.color.progress_orange));
                    progressView.setColor(getResources().getColor(R.color.progress_orange));
                } else if (temData > 37) {
                    tvTemWarning.setText("体温：" + temData + ",体温偏高");
                    tvTemWarning.setTextColor(getResources().getColor(R.color.progress_red));
                    progressView.setColor(getResources().getColor(R.color.progress_red));

                } else {
                    tvTemWarning.setText("体温：" + temData + ",体温正常");
                    tvTemWarning.setTextColor(getResources().getColor(R.color.progress_green));
                    progressView.setColor(getResources().getColor(R.color.progress_green));
                }

                int angle = 37 * 330 / 45;
                progressView.setAngleWithAnim(angle);
            }
        });
    }

    @OnClick({R.id.btn_start_measure_tem, R.id.btn_start_measure_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_measure_tem:
                if (!isMeasureTemp) {
                    btnStartMeasureSave.setVisibility(View.GONE);
                    MonitorDataTransmissionManager.getInstance().startMeasure(MeasureType.BT);
                    MonitorDataTransmissionManager.getInstance().setOnBtResultListener(this);
                    btnStartMeasureTem.setText("测量中...");
                    isMeasureTemp = true;
                }
                break;
            case R.id.btn_start_measure_save:
                try{
                    long time = System.currentTimeMillis();
                    dbManager.addTempMessage(time + "", tvTemData.getText().toString());
                    Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getActivity(), "保存异常"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
