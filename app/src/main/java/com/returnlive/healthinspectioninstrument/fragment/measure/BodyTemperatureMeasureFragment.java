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
import com.linktop.infs.OnBtResultListener;
import com.linktop.whealthService.MeasureType;
import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.bean.health_updata.ecg_bean.DetectItem;
import com.returnlive.healthinspectioninstrument.bean.health_updata.tem.TemperatureMeasuredData;
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
    private TemperatureMeasuredData temperatureMeasuredData;
    private Gson gson;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_body_temperature_measure, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        gson = new Gson();
        temperatureMeasuredData = new TemperatureMeasuredData();
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
                showIOSLodingDialog("正在保存...");

                //体温
                DetectItem temperatureItem = new DetectItem();
                temperatureItem.setValue(Float.valueOf(tvTemData.getText().toString().trim()));
                temperatureItem.setUnit("℃");
                temperatureMeasuredData.setTemperature(temperatureItem);


                try {
                    long time = System.currentTimeMillis();
                    dbManager.addTempMessage(time + "", tvTemData.getText().toString());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            tmeInterface(temperatureMeasuredData);
                        }
                    }).start();
                } catch (Exception e) {
                    dismissIOSDialog();
                    Toast.makeText(getActivity(), "保存异常" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //体温上传
    private void tmeInterface(TemperatureMeasuredData temperatureMeasuredData) {
        OkHttpUtils.post().url(UP_DATA_HEALTH + midUrl)
                .addParams("data", gson.toJson(temperatureMeasuredData))
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
                        temHandler.sendMessage(msg);
                    }
                });
    }


    private Handler temHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(Code.SUCCESS) > 0) {
                dismissIOSDialog();
                btnStartMeasureTem.setText("开始");
                btnStartMeasureSave.setVisibility(View.GONE);
                Toast.makeText(getActivity(), getResources().getString(R.string.save_success), Toast.LENGTH_SHORT).show();
            } else {
                dismissIOSDialog();
                try {
                    returnCode = GsonParsing.sendCodeError(result);
                } catch (Exception e) {
                    Log.e(TAG, "体温异常: " + e.getMessage());
                }
                getCodeStatus(returnCode.getCode());
            }
        }
    };

}
