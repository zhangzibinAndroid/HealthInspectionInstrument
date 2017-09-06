package com.returnlive.healthinspectioninstrument.adapter.other;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linktop.whealthService.OnBLEService;
import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.base.MyBaseAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DialogBlueScanAdapter extends MyBaseAdapter<OnBLEService.DeviceSort> implements View.OnClickListener {
    public DialogBlueScanAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_text_bluetooth, null);
            viewHolder = new ViewHolder(view);
        }
        OnBLEService.DeviceSort bean = list.get(i);
        viewHolder.tvBluetoothName.setText("蓝牙名称：" + bean.bleDevice.getName() + "\n蓝牙地址：" + bean.bleDevice.getAddress());
        view.setTag(bean);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        onItemClickWithDataListener.OnItemClickWithData((OnBLEService.DeviceSort) view.getTag());
    }

    static class ViewHolder {
        @BindView(R.id.tv_bluetooth_name)
        TextView tvBluetoothName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    private OnItemClickWithDataListener onItemClickWithDataListener;

    public static interface OnItemClickWithDataListener {
        void OnItemClickWithData(OnBLEService.DeviceSort deviceSort);
    }

    public void setOnItemClickWithDataListener(OnItemClickWithDataListener onItemClickWithDataListener) {
        this.onItemClickWithDataListener = onItemClickWithDataListener;
    }
}
