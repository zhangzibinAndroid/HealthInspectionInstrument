package com.returnlive.healthinspectioninstrument.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.MyBaseAdapter;
import com.returnlive.healthinspectioninstrument.bean.DbEcgBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/30 0030
 * 时间： 下午 1:31
 * 描述： 心电历史数据
 */

public class EcgHistoryAdapter extends MyBaseAdapter<DbEcgBean> {
    public EcgHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_ecg_lv, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            AutoUtils.autoSize(view);

        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        DbEcgBean dbEcgBean = list.get(position);
        String date =new  SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(Long.valueOf(dbEcgBean.getTime())));
        viewHolder.tvTime.setText(date);
        viewHolder.tvRrMaxHistory.setText(dbEcgBean.getRr_max());
        viewHolder.tvRrMinHistory.setText(dbEcgBean.getRr_min());
        viewHolder.tvMoodHistory.setText(dbEcgBean.getMoods());
        viewHolder.tvHeartRateHistory.setText(dbEcgBean.getHr());
        viewHolder.tvHeartRateVariabilityHistory.setText(dbEcgBean.getHrv());
        viewHolder.tvBreathingRateHistory.setText(dbEcgBean.getBrs());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_rr_max_history)
        TextView tvRrMaxHistory;
        @BindView(R.id.tv_rr_min_history)
        TextView tvRrMinHistory;
        @BindView(R.id.tv_mood_history)
        TextView tvMoodHistory;
        @BindView(R.id.tv_heart_rate_history)
        TextView tvHeartRateHistory;
        @BindView(R.id.tv_heart_rate_variability_history)
        TextView tvHeartRateVariabilityHistory;
        @BindView(R.id.tv_breathing_rate_history)
        TextView tvBreathingRateHistory;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
