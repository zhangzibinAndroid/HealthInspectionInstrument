package com.returnlive.healthinspectioninstrument.fragment.history;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.adapter.BloodPreAdapter;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.bean.DbBloodPreBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/28 0028
 * 时间： 下午 12:57
 * 描述： 历史血压
 */
public class BloodPressureFragment extends BaseFragment {

    @BindView(R.id.lv_blood_pre)
    ListView lvBloodPre;
    private ArrayList<DbBloodPreBean> preList;
    private BloodPreAdapter bloodPreAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blood_pressure, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        bloodPreAdapter = new BloodPreAdapter(getActivity());
        lvBloodPre.setAdapter(bloodPreAdapter);
        preList = dbManager.searchBloodPreData();
        /*DbBloodPreBean dbBloodPreBean2 = new DbBloodPreBean("19987456321","89","140");
        bloodPreAdapter.addDATA(dbBloodPreBean2);

        DbBloodPreBean dbBloodPreBean3 = new DbBloodPreBean("19987456321","90","14");
        bloodPreAdapter.addDATA(dbBloodPreBean3);*/

        for (int i = preList.size()-1; i >-1 ; i--) {
            DbBloodPreBean dbBloodPreBean = preList.get(i);
            bloodPreAdapter.addDATA(dbBloodPreBean);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
