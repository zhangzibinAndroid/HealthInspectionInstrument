package com.returnlive.healthinspectioninstrument.fragment.measure;


import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.linktop.whealthService.task.Bp_task;
import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.view.ProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/29 0029
 * 时间： 下午 4:21
 * 描述： 血压测量
 */
public class BloodPressureMeasureFragment extends BaseFragment {


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blood_pressure_measure, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }
    private void initView() {
        tvDiastolicWarning.setVisibility(View.INVISIBLE);
        setBloodPressureDataCallBack(bloodPressureCall);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_start_measure_tem)
    public void onViewClicked() {
        if (bp_button_state == START) {
            mHealthApi.startBP();
            bp_button_state = STOP;
            tvSystolicWarning.setText("正在测量，请保持安静...");
            btnStartMeasureTem.setText("停止");
        } else {
            mHealthApi.stopBP();
            bp_button_state = START;
            btnStartMeasureTem.setText("开始");
        }
    }

    private BloodPressureDataCallBack bloodPressureCall = new BloodPressureDataCallBack() {
        @Override
        public void getBloodPressureDataCallBack(Message msg) {
            switch (msg.what) {
                case Bp_task.BP_RESULT:
                    int systolic = msg.arg1;//收缩压
                    int diastolic = msg.arg2;//舒张压
                    bp_button_state = START;
                    btnStartMeasureTem.setText("开始");
                    tvSystolicWarning.setText("收缩压: "+systolic+"mmHg");
                    tvDiastolicWarning.setVisibility(View.VISIBLE);
                    tvDiastolicWarning.setText("舒张压: "+diastolic+"mmHg");
                    Log.e(TAG, "收缩压: "+systolic );
                    Log.e(TAG, "舒张压: "+diastolic );
                    break;
                case Bp_task.LOUQI:
                    tvDiastolicWarning.setVisibility(View.INVISIBLE);
                    tvSystolicWarning.setText("设备泄漏");
                    Toast.makeText(getActivity(), "泄露", Toast.LENGTH_SHORT).show();
                    bp_button_state = START;
                    btnStartMeasureTem.setText("开始");
                    break;
                default:
                    break;
            }
        }
    };
}
