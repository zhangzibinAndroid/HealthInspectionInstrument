package com.returnlive.healthinspectioninstrument.fragment.history;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.adapter.TempHistoryAdapter;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.bean.DbTempBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/28 0028
 * 时间： 下午 1:00
 * 描述： 历史体温
 */
public class BodyTemperatureFragment extends BaseFragment {

    @BindView(R.id.lv_temp)
    ListView lvTemp;
    private TempHistoryAdapter tempHistoryAdapter;
    private ArrayList<DbTempBean> tempList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_body_temperature, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tempHistoryAdapter = new TempHistoryAdapter(getActivity());
        lvTemp.setAdapter(tempHistoryAdapter);
        tempList = dbManager.searchTempData();
        for (int i = tempList.size()-1; i >-1 ; i--) {
            DbTempBean dbTempBean = tempList.get(i);
            tempHistoryAdapter.addDATA(dbTempBean);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



}
