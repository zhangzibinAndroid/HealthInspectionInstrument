package com.returnlive.healthinspectioninstrument.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.MyBaseAdapter;
import com.returnlive.healthinspectioninstrument.bean.DbBloodOxBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/31 0031
 * 时间： 上午 11:43
 * 描述： 血氧历史数据适配器
 */
public class BloodOxAdapter extends MyBaseAdapter<DbBloodOxBean> {
    public BloodOxAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_blood_ox_lv, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            AutoUtils.autoSize(view);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        DbBloodOxBean dbBloodOxBean = list.get(i);
        String date =new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(Long.valueOf(dbBloodOxBean.getTime())));
        viewHolder.tvTimeBloodOx.setText(date);
        viewHolder.tvBloodOxHistory.setText("血氧:" +dbBloodOxBean.getBloodOx()+"%");
        viewHolder.tvHrHistory.setText("心率:" + dbBloodOxBean.getHr()+"次");
        int bo = Integer.parseInt(dbBloodOxBean.getBloodOx());
        if (bo<95){
            viewHolder.tvBloodOxWarning.setText("血氧偏低");
            viewHolder.tvBloodOxWarning.setTextColor(context.getResources().getColor(R.color.progress_orange));
        }else if (bo>99){
            viewHolder.tvBloodOxWarning.setText("血氧偏高");
            viewHolder.tvBloodOxWarning.setTextColor(context.getResources().getColor(R.color.progress_red));
        }else {
            viewHolder.tvBloodOxWarning.setText("血氧正常");
            viewHolder.tvBloodOxWarning.setTextColor(context.getResources().getColor(R.color.tv_time_color));
        }

        int hr = Integer.parseInt(dbBloodOxBean.getHr());
        if (hr<60){
            viewHolder.tvHrWarning.setText("心率偏低");
            viewHolder.tvHrWarning.setTextColor(context.getResources().getColor(R.color.progress_orange));
        }else if (hr>100){
            viewHolder.tvHrWarning.setText("心率偏高");
            viewHolder.tvHrWarning.setTextColor(context.getResources().getColor(R.color.progress_red));
        }else {
            viewHolder.tvHrWarning.setText("心率正常");
            viewHolder.tvHrWarning.setTextColor(context.getResources().getColor(R.color.tv_time_color));
        }
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.tv_time_blood_ox)
        TextView tvTimeBloodOx;
        @BindView(R.id.tv_blood_ox_history)
        TextView tvBloodOxHistory;
        @BindView(R.id.tv_hr_history)
        TextView tvHrHistory;
        @BindView(R.id.tv_blood_ox_warning)
        TextView tvBloodOxWarning;
        @BindView(R.id.tv_hr_warning)
        TextView tvHrWarning;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
