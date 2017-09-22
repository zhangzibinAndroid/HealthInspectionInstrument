package com.returnlive.healthinspectioninstrument.fragment.menu;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.BaseFragment;
import com.returnlive.healthinspectioninstrument.bean.EventMessage;
import com.returnlive.healthinspectioninstrument.constant.UserMessage;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/28 0028
 * 时间： 下午 12:23
 * 描述： 我的
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvUserPhone.setText(UserMessage.phone);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.btn_exit)
    public void onViewClicked() {
        EventBus.getDefault().post(new EventMessage("exit"));

    }
}
