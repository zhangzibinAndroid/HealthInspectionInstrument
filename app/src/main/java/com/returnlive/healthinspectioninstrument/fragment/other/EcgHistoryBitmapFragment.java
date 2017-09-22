package com.returnlive.healthinspectioninstrument.fragment.other;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.bean.DbEcgBean;
import com.returnlive.healthinspectioninstrument.gson.GsonParsing;
import com.returnlive.healthinspectioninstrument.utils.DataGetUtils;
import com.returnlive.healthinspectioninstrument.view.other.EcgWaveView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/30 0030
 * 时间： 下午 5:13
 * 描述： 历史数据心电图展示
 */
public class EcgHistoryBitmapFragment extends BaseFragment {


    @BindView(R.id.ecg_view_history)
    EcgWaveView ecgViewHistory;
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
    /*@BindView(R.id.tv_breathing_rate)
    TextView tvBreathingRate;*/
    @BindView(R.id.btn_start_measure)
    Button btnStartMeasure;
    private DbEcgBean dbEcgBean;
    private List<Integer> ecgDataList;
    private List<ArrayList<Integer>> list = new ArrayList<>();
    private float mWidth = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ecg_history_bitmap, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        ecgViewHistory.setPagerSpeed(1);
        ecgViewHistory.setCalibration(1.0f);
        mWidth = 0;
        btnStartMeasure.setVisibility(View.GONE);
        dbEcgBean = DataGetUtils.ecgBean;
        String jsonData = dbEcgBean.getJsonData();
        try {
            ecgDataList = GsonParsing.getEcgJson(jsonData);
            Log.e(TAG, "initView: "+ecgDataList );
            ecgViewHistory.clear();
            ecgViewHistory.preparePoints(ecgDataList);

        } catch (Exception e) {
            Log.e(TAG, "解析失败" + e.getMessage());
        }

        tvRrMax.setText(dbEcgBean.getRr_max());
        tvRrMin.setText(dbEcgBean.getRr_min());
        tvMood.setText(dbEcgBean.getMoods());
        tvHeartRate.setText( dbEcgBean.getHr());
        tvHeartRateVariability.setText( dbEcgBean.getHrv());
//        tvBreathingRate.setText(dbEcgBean.getBrs());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
