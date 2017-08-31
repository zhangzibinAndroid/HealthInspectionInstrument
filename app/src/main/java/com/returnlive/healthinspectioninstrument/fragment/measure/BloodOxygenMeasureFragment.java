package com.returnlive.healthinspectioninstrument.fragment.measure;


import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.linktop.whealthService.task.Ox_task;
import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
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
public class BloodOxygenMeasureFragment extends BaseFragment {


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blood_oxygen_measure, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvHrWarning.setVisibility(View.INVISIBLE);
        setOxygenCallBack(oxygenCall);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (ox_button_state == START){
            mHealthApi.stopOX();
            ox_button_state = START;
            btnStartMeasureOxygen.setText("开始");
        }
        unbinder.unbind();
    }

    @OnClick(R.id.btn_start_measure_oxygen)
    public void onViewClicked() {
        if (ox_button_state == START) {
            mHealthApi.startOX();
            ox_button_state = STOP;
            btnStartMeasureOxygen.setText("停止");
        } else {
            mHealthApi.stopOX();
            ox_button_state = START;
            btnStartMeasureOxygen.setText("开始");
        }
    }

    private OxygenCallBack oxygenCall = new OxygenCallBack() {
        @Override
        public void getOxygenCall(Message msg) {
            switch (msg.what) {
                case Ox_task.OXYGEN_TEST_FINISH://血氧测量结果
                    double[] data = msg.getData().getDoubleArray("oxyResult");
                    if (data != null) {
                        tvHrWarning.setVisibility(View.VISIBLE);
                        int oxyData = (int) (data[1] + 0.5);
                        int hrData = (int) data[0];
                        tvOxygenData.setText(oxyData+"/"+hrData);
                        btnStartMeasureOxygen.setText("开始");
                        ox_button_state = START;
                        if (oxyData<95){
                            tvOxygenWarning.setText("血氧:" + oxyData +"%,血氧偏低");
                            tvOxygenWarning.setTextColor(getResources().getColor(R.color.progress_yellow));
                            progressViewOxygen.setColor(getResources().getColor(R.color.progress_yellow));

                        }else if (oxyData>99){
                            tvOxygenWarning.setText("血氧:" + oxyData +"%,血氧偏高");
                            tvOxygenWarning.setTextColor(getResources().getColor(R.color.progress_orange));
                            progressViewOxygen.setColor(getResources().getColor(R.color.progress_orange));

                        }else {
                            tvOxygenWarning.setText("血氧:" + oxyData +"%,血氧正常");
                            tvOxygenWarning.setTextColor(getResources().getColor(R.color.progress_green));
                            progressViewOxygen.setColor(getResources().getColor(R.color.progress_green));
                        }

                        if (hrData<60){
                            tvHrWarning.setText("心率:" + hrData+"次,心率偏低");
                            tvHrWarning.setTextColor(getResources().getColor(R.color.progress_yellow));
                        }else if (hrData>100){
                            tvHrWarning.setText("心率:" + hrData+"次,心率偏高");
                            tvHrWarning.setTextColor(getResources().getColor(R.color.progress_orange));
                        }else {
                            tvHrWarning.setText("心率:" + hrData+"次,心率正常");
                            tvHrWarning.setTextColor(getResources().getColor(R.color.progress_green));
                        }

                        int angle = oxyData*320/100;
                        progressViewOxygen.setAngleWithAnim(angle);
                        final long timeData = System.currentTimeMillis();
                        String time = timeData + "";
                        dbManager.addBloodOxMessage(time,oxyData+"",hrData+"");
                    }
                    break;
                case Ox_task.AXIS_DATA://血氧数据
                    int red_value = msg.getData().getInt("oxDataRed");
                    tvOxygenWarning.setText("正在测量："+red_value);
                    tvHrWarning.setVisibility(View.INVISIBLE);
                    break;

                case Ox_task.OX_CMD_TIMEOUT://血氧测试超时
                    int ox_cmd_type = msg.getData().getInt("ox_cmd_type");
                    tvOxygenWarning.setText("血氧测试超时 " + ox_cmd_type);
                    btnStartMeasureOxygen.setText("开始");
                    break;
                default:
                    break;
            }
        }
    };
}
