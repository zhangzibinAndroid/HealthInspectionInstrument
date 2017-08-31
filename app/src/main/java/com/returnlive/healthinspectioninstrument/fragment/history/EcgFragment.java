package com.returnlive.healthinspectioninstrument.fragment.history;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.activity.DataDealActivity;
import com.returnlive.healthinspectioninstrument.adapter.EcgHistoryAdapter;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.bean.DbEcgBean;
import com.returnlive.healthinspectioninstrument.bean.EventMessage;
import com.returnlive.healthinspectioninstrument.utils.DataGetUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/28 0028
 * 时间： 下午 1:07
 * 描述： 历史心电
 */
public class EcgFragment extends BaseFragment {

    @BindView(R.id.lv_ecg)
    ListView lvEcg;

    private EcgHistoryAdapter ecgHistoryAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ecg, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        ecgHistoryAdapter = new EcgHistoryAdapter(getActivity());
        lvEcg.setAdapter(ecgHistoryAdapter);
        ArrayList<DbEcgBean> list = dbManager.searchEcgData();
        for (int i = list.size()-1; i >-1 ; i--) {
            DbEcgBean dbEcgBean = list.get(i);
            ecgHistoryAdapter.addDATA(dbEcgBean);
        }
        lvEcg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DataGetUtils.ecgBean = list.get(position);
                EventBus.getDefault().postSticky(new EventMessage("ecg_history"));
                JumpActivity(DataDealActivity.class);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
