package com.returnlive.healthinspectioninstrument.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.MyBaseAdapter;
import com.returnlive.healthinspectioninstrument.bean.DbBloodPreBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/31 0031
 * 时间： 下午 12:33
 * 描述： 血压历史数据适配器
 */
public class BloodPreAdapter extends MyBaseAdapter<DbBloodPreBean> {
    public BloodPreAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_blood_pre_lv, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            AutoUtils.autoSize(view);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        DbBloodPreBean dbBloodPreBean = list.get(i);
        String date = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(Long.valueOf(dbBloodPreBean.getTime())));
        viewHolder.tvTimeBloodPre.setText(date);
        viewHolder.tvBloodPreSys.setText("收缩压: " + dbBloodPreBean.getBloodPreSys() + "mmHg");
        viewHolder.tvBloodPreDia.setText("舒张压: " + dbBloodPreBean.getBloodPreDia() + "mmHg");
        int sys = Integer.parseInt(dbBloodPreBean.getBloodPreSys());
        int dia = Integer.parseInt(dbBloodPreBean.getBloodPreDia());

        if (sys<90){
            viewHolder.tvBloodPreSysWarning.setText("收缩压偏低");
            viewHolder.tvBloodPreSysWarning.setTextColor(context.getResources().getColor(R.color.progress_orange));
        }else if (sys>139){
            viewHolder.tvBloodPreSysWarning.setText("收缩压偏高");
            viewHolder.tvBloodPreSysWarning.setTextColor(context.getResources().getColor(R.color.progress_red));
        }else {
            viewHolder.tvBloodPreSysWarning.setText("收缩压正常");
            viewHolder.tvBloodPreSysWarning.setTextColor(context.getResources().getColor(R.color.tv_time_color));
        }


        if (dia<90){
            viewHolder.tvPreDiaWarning.setText("舒张压偏低");
            viewHolder.tvPreDiaWarning.setTextColor(context.getResources().getColor(R.color.progress_orange));
        }else if (dia>139){
            viewHolder.tvPreDiaWarning.setText("舒张压偏高");
            viewHolder.tvPreDiaWarning.setTextColor(context.getResources().getColor(R.color.progress_red));
        }else {
            viewHolder.tvPreDiaWarning.setText("舒张压正常");
            viewHolder.tvPreDiaWarning.setTextColor(context.getResources().getColor(R.color.tv_time_color));
        }

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_time_blood_pre)
        TextView tvTimeBloodPre;
        @BindView(R.id.tv_blood_pre_sys)
        TextView tvBloodPreSys;
        @BindView(R.id.tv_blood_pre_dia)
        TextView tvBloodPreDia;
        @BindView(R.id.tv_blood_pre_sys_warning)
        TextView tvBloodPreSysWarning;
        @BindView(R.id.tv_pre_dia_warning)
        TextView tvPreDiaWarning;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
