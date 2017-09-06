package com.returnlive.healthinspectioninstrument.fragment.other;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.linktop.MonitorDataTransmissionManager;
import com.linktop.whealthService.OnBLEService;
import com.returnlive.healthinspectioninstrument.R;
import com.returnlive.healthinspectioninstrument.adapter.other.DialogBlueScanAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 作者： 张梓彬
 * 日期： 2017/9/4 0004
 * 时间： 下午 2:46
 * 描述： 蓝牙扫描列表
 */

public class BlueDialogListFragment extends DialogFragment {
    @BindView(R.id.lv_blue_scan)
    ListView lvBlueScan;
    private Unbinder unbinder;
    private List<OnBLEService.DeviceSort> blueDeceiveList;
    private DialogBlueScanAdapter dialogBlueScanAdapter;
    private Thread thread;
    private static final String TAG = "BlueDialogListFragment";
    public static boolean isBlueConnect = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_blue_scan, container);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;

    }

    private void initView() {
        dialogBlueScanAdapter = new DialogBlueScanAdapter(getActivity());
        lvBlueScan.setAdapter(dialogBlueScanAdapter);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!MonitorDataTransmissionManager.getInstance().isScanning()) {
                    MonitorDataTransmissionManager.getInstance().autoScan(true);
                    while (true) {
                        blueDeceiveList = MonitorDataTransmissionManager.getInstance().getDeviceList();
                        if (blueDeceiveList.size() > 0) {
                            MonitorDataTransmissionManager.getInstance().autoScan(false);
                            break;
                        }
                    }
                    dialogBlueScanAdapter.addAllDataToMyadapterWithoutClean(blueDeceiveList);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogBlueScanAdapter.notifyDataSetChanged();
                        }
                    });
                }

            }


        });
        thread.start();
        dialogBlueScanAdapter.setOnItemClickWithDataListener(new DialogBlueScanAdapter.OnItemClickWithDataListener() {
            @Override
            public void OnItemClickWithData(OnBLEService.DeviceSort deviceSort) {
                if (!isBlueConnect) {
                    MonitorDataTransmissionManager.getInstance().connectToBle(deviceSort.bleDevice);
                    dismiss();
                }

            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        MonitorDataTransmissionManager.getInstance().autoScan(false);
    }


}
