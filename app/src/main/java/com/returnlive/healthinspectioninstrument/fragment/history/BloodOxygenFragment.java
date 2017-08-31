package com.returnlive.healthinspectioninstrument.fragment.history;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.adapter.BloodOxAdapter;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.bean.DbBloodOxBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/28 0028
 * 时间： 下午 12:59
 * 描述： 历史血氧
 */
public class BloodOxygenFragment extends BaseFragment {

    @BindView(R.id.lv_blood_ox)
    ListView lvBloodOx;
    private ArrayList<DbBloodOxBean> bloodOxList;
    private BloodOxAdapter bloodOxAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_blood_oxygen, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        bloodOxList = dbManager.searchBloodOxData();
        bloodOxAdapter = new BloodOxAdapter(getActivity());
        lvBloodOx.setAdapter(bloodOxAdapter);
        for (int i = bloodOxList.size() - 1; i > -1; i--) {
            DbBloodOxBean dbBloodOxBean = bloodOxList.get(i);
            bloodOxAdapter.addDATA(dbBloodOxBean);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
