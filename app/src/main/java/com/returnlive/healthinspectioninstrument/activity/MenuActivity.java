package com.returnlive.healthinspectioninstrument.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.BaseActivity;
import com.returnlive.healthinspectioninstrument.fragment.menu.HistoryFragment;
import com.returnlive.healthinspectioninstrument.fragment.menu.HomePageFragment;
import com.returnlive.healthinspectioninstrument.fragment.menu.MineFragment;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuActivity extends BaseActivity {

    private static final String TAG = "TAG";
    @BindViews({R.id.tv_home_page, R.id.tv_history, R.id.tv_mine})
    TextView[] tv_sel;
    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
    private HomePageFragment homePageFragment;
    private HistoryFragment historyFragment;
    private MineFragment mineFragment;
    private long exitTime = 0;//点击2次返回，退出程序

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        initPermission(permissions);
        initView();
    }


    //初始化页面
    private void initView() {
        homePageFragment = new HomePageFragment();
        historyFragment = new HistoryFragment();
        mineFragment = new MineFragment();
        setReplaceFragment(R.id.lay_fragment, homePageFragment);
        tv_sel[0].setSelected(true);
        tv_sel[0].setTextColor(getResources().getColor(R.color.text_pressed_blue));
    }


    @OnClick({R.id.tv_home_page, R.id.tv_history, R.id.tv_mine})
    public void onViewClicked(View view) {
        for (int i = 0; i < tv_sel.length; i++) {
            tv_sel[i].setSelected(false);
            tv_sel[i].setTextColor(getResources().getColor(R.color.text_default_color));
        }

        switch (view.getId()) {
            case R.id.tv_home_page://首页
                tv_sel[0].setSelected(true);
                tv_sel[0].setTextColor(getResources().getColor(R.color.text_pressed_blue));
                setReplaceFragment(R.id.lay_fragment, homePageFragment);
                break;
            case R.id.tv_history://历史
                tv_sel[1].setSelected(true);
                tv_sel[1].setTextColor(getResources().getColor(R.color.text_pressed_blue));
                setReplaceFragment(R.id.lay_fragment, historyFragment);
                break;
            case R.id.tv_mine://我的
                tv_sel[2].setSelected(true);
                tv_sel[2].setTextColor(getResources().getColor(R.color.text_pressed_blue));
                setReplaceFragment(R.id.lay_fragment, mineFragment);
                break;
        }
    }



    //点击两次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {//两秒内再次点击返回则退出
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
