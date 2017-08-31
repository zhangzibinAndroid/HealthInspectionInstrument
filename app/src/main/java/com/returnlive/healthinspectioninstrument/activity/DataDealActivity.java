package com.returnlive.healthinspectioninstrument.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.BaseActivity;
import com.returnlive.healthinspectioninstrument.bean.EventMessage;
import com.returnlive.healthinspectioninstrument.fragment.measure.BloodOxygenMeasureFragment;
import com.returnlive.healthinspectioninstrument.fragment.measure.BloodPressureMeasureFragment;
import com.returnlive.healthinspectioninstrument.fragment.measure.BodyTemperatureMeasureFragment;
import com.returnlive.healthinspectioninstrument.fragment.measure.EcgMeasureFragment;
import com.returnlive.healthinspectioninstrument.fragment.other.EcgHistoryBitmapFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/28 0028
 * 时间： 下午 4:56
 * 描述： 测量页面
 */
public class DataDealActivity extends BaseActivity {
    private static final String TAG = "DataDealActivity";
    private boolean isClicked = true;
    private EcgMeasureFragment ecgMeasureFragment;
    private BodyTemperatureMeasureFragment bodyTemperatureMeasureFragment;
    private BloodOxygenMeasureFragment bloodOxygenMeasureFragment;
    private BloodPressureMeasureFragment bloodPressureMeasureFragment;
    private EcgHistoryBitmapFragment ecgHistoryBitmapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_measure);
        ButterKnife.bind(this);
        initView();
        if (isClicked) {
            isClicked = false;
            EventBus.getDefault().register(this);//接收粘性数据
        }


    }

    private void initView() {
        ecgMeasureFragment = new EcgMeasureFragment();
        bodyTemperatureMeasureFragment = new BodyTemperatureMeasureFragment();
        bloodOxygenMeasureFragment = new BloodOxygenMeasureFragment();
        bloodPressureMeasureFragment = new BloodPressureMeasureFragment();
        ecgHistoryBitmapFragment = new EcgHistoryBitmapFragment();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();//移除所有粘性事件
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMessage(EventMessage eventMessage) {
        Log.e(TAG, "getMessage: " + eventMessage.getMessage());
        if (eventMessage.getMessage().equals("ecg")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setReplaceFragment(R.id.lay_fragment_measure, ecgMeasureFragment);
        } else if (eventMessage.getMessage().equals("blood_pressure")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setReplaceFragment(R.id.lay_fragment_measure, bloodPressureMeasureFragment);

        }else if (eventMessage.getMessage().equals("blood_oxygen")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setReplaceFragment(R.id.lay_fragment_measure, bloodOxygenMeasureFragment);

        }else if (eventMessage.getMessage().equals("body_temperature")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setReplaceFragment(R.id.lay_fragment_measure, bodyTemperatureMeasureFragment);
        }else if (eventMessage.getMessage().equals("ecg_history")){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setReplaceFragment(R.id.lay_fragment_measure, ecgHistoryBitmapFragment);
        }
    }

    @OnClick(R.id.toolbar)
    public void onViewClicked() {
        finish();
    }
}
