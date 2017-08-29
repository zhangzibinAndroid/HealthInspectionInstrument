package com.returnlive.healthinspectioninstrument.fragment.history;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/28 0028
 * 时间： 下午 12:59
 * 描述： 历史血氧
 */
public class BloodOxygenFragment extends BaseFragment {


    public BloodOxygenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_blood_oxygen, container, false);
        return view;
    }

}
