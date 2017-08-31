package com.returnlive.healthinspectioninstrument.fragment.measure;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
public class BodyTemperatureMeasureFragment extends BaseFragment {


    @BindView(R.id.tv_tem_data)
    TextView tvTemData;
    @BindView(R.id.btn_start_measure_tem)
    Button btnStartMeasureTem;
    @BindView(R.id.progress_view)
    ProgressView progressView;
    @BindView(R.id.tv_tem_warning)
    TextView tvTemWarning;

    private boolean isStartMeasure = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_body_temperature_measure, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        isStartMeasure = false;
        setTmepCallBack(tmepCallBack);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_start_measure_tem)
    public void onViewClicked() {
        if (!isStartMeasure) {
            mHealthApi.startBT();
            bt_button_state = STOP;
            btnStartMeasureTem.setText("测量中...");
            isStartMeasure = true;
        } else {
//            btnStartMeasureTem.setText("开始");
            isStartMeasure = false;
        }


    }

    private TmepCallBack tmepCallBack = new TmepCallBack() {
        @Override
        public void getTmepCallBack(String data, double temData) {
            tvTemData.setText(data);
            final long timeData = System.currentTimeMillis();
            String time = timeData + "";
            dbManager.addTempMessage(time,data);
            btnStartMeasureTem.setText("开始");
            isStartMeasure = false;
            double temp = Double.valueOf(data);
            Log.e(TAG, "temp: "+temp );
            int angle = (int) ((temData / 50) * 360);
            if (temp < 36) {
                tvTemWarning.setText("体温偏低");
                tvTemWarning.setTextColor(getResources().getColor(R.color.progress_yellow));
                progressView.setColor(getResources().getColor(R.color.progress_yellow));
            } else if (temp > 37) {
                tvTemWarning.setText("体温偏高");
                tvTemWarning.setTextColor(getResources().getColor(R.color.progress_red));
                progressView.setColor(getResources().getColor(R.color.progress_red));
            } else {
                tvTemWarning.setText("体温正常");
                tvTemWarning.setTextColor(getResources().getColor(R.color.progress_green));
                progressView.setColor(getResources().getColor(R.color.progress_green));

            }
            progressView.setAngleWithAnim(angle);

        }
    };
}
