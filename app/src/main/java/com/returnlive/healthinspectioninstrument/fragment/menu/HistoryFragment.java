package com.returnlive.healthinspectioninstrument.fragment.menu;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.adapter.HistoryFragmentAdapter;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.fragment.history.BloodOxygenFragment;
import com.returnlive.healthinspectioninstrument.fragment.history.BloodPressureFragment;
import com.returnlive.healthinspectioninstrument.fragment.history.BodyTemperatureFragment;
import com.returnlive.healthinspectioninstrument.fragment.history.EcgFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/28 0028
 * 时间： 下午 12:22
 * 描述： 历史
 */
public class HistoryFragment extends BaseFragment {
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    private HistoryFragmentAdapter historyFragmentAdapter;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> nameList = new ArrayList<>();

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        EcgFragment ecgFragment = new EcgFragment();
        BloodPressureFragment bloodPressureFragment = new BloodPressureFragment();
        BloodOxygenFragment bloodOxygenFragment = new BloodOxygenFragment();
        BodyTemperatureFragment bodyTemperatureFragment = new BodyTemperatureFragment();
        if (nameList.size()>0){

        }else {
            nameList.add(getResources().getString(R.string.ecg));
            nameList.add(getResources().getString(R.string.blood_pressure));
            nameList.add(getResources().getString(R.string.blood_oxygen));
            nameList.add(getResources().getString(R.string.body_temperature));
            fragmentList.add(ecgFragment);
            fragmentList.add(bloodPressureFragment);
            fragmentList.add(bloodOxygenFragment);
            fragmentList.add(bodyTemperatureFragment);
        }
        historyFragmentAdapter = new HistoryFragmentAdapter(getChildFragmentManager(),nameList,fragmentList);
        viewPager.setAdapter(historyFragmentAdapter);
        //注：ViewPager滑动时绑定TabLayout进行联动效果
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
