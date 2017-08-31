package com.returnlive.healthinspectioninstrument.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.MyBaseAdapter;
import com.returnlive.healthinspectioninstrument.bean.DbTempBean;
import com.zhy.autolayout.utils.AutoUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/31 0031
 * 时间： 上午 10:52
 * 描述： 体温历史数据适配器
 */

public class TempHistoryAdapter extends MyBaseAdapter<DbTempBean> {
    public TempHistoryAdapter(Context context) {
        super(context);

    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_temp_lv, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            AutoUtils.autoSize(view);

        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        DbTempBean dbTempBean = list.get(position);
        String date =new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(Long.valueOf(dbTempBean.getTime())));
        viewHolder.tvTimeTemp.setText(date);
        viewHolder.tvTempData.setText("体温："+dbTempBean.getData()+"℃");
        double temp = Double.valueOf(dbTempBean.getData());
        if (temp<36){
            viewHolder.tvTempWarning.setText("体温偏低");
            viewHolder.tvTempWarning.setTextColor(context.getResources().getColor(R.color.progress_orange));
        }else if (temp>37){
            viewHolder.tvTempWarning.setText("体温偏高");
            viewHolder.tvTempWarning.setTextColor(context.getResources().getColor(R.color.progress_red));
        }else {
            viewHolder.tvTempWarning.setText("体温正常");
            viewHolder.tvTempWarning.setTextColor(context.getResources().getColor(R.color.tv_time_color));
        }



        return view;
    }

    static class ViewHolder {
        @BindView(R.id.tv_time_temp)
        TextView tvTimeTemp;
        @BindView(R.id.tv_temp_data)
        TextView tvTempData;
        @BindView(R.id.tv_temp_warning)
        TextView tvTempWarning;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
