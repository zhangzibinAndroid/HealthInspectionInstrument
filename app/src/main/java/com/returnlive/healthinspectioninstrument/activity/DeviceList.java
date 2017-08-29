package com.returnlive.healthinspectioninstrument.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.linktop.whealthService.HealthApi;
import com.linktop.whealthService.OnBLEService;
import com.returnlive.healthinspectioninstrument.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class DeviceList extends Activity implements HealthApi.BleCallBack {
    private BluetoothAdapter mBtAdapter;
    private TextView mEmptyList;
    public static final String TAG = "DeviceListActivity";
    private OnBLEService mService = null;
    //    List<BluetoothDevice> deviceList;
    ArrayList<BluetoothDevice> deviceList;
    ArrayList<devicesort> devsorts;
    devicesort devsort;
    private DeviceAdapter deviceAdapter;
    private DeviceAdapterSort deviceAdapterSort;
    public int somevalue = 10;
    Map<String, Integer> devRssiValues;
    BluetoothDevice device;
    boolean scanning = false;

    private Timer timer;
    private boolean refresh = true;


    private HealthApi mHealthApi;


    public static class devicesort {
        BluetoothDevice bleDevice;
        int rssi;
    }

    ComparatorUser comparator = new ComparatorUser();

    public static class ComparatorUser implements Comparator<devicesort> {

        @Override
        public int compare(devicesort lhs, devicesort rhs) {
            // TODO Auto-generated method stub
            if (lhs.rssi < rhs.rssi)
                return 1;
            else if (lhs.rssi == rhs.rssi)
                return 0;
            else
                return -1;
        }

//		@Override
//		public int compare(Object lhs, Object rhs) {
//			// TODO Auto-generated method stub
//			devicesort one = (devicesort) lhs;
//			devicesort two = (devicesort) rhs;
//			if(one.rssi < two.rssi)
//				return 1;
//			else if(one.rssi == two.rssi)
//				return 0;
//			else
//				return -1;
//		}

    }


    @Override
    public void bleCallBack(int type, Object data) {
        // TODO Auto-generated method stub
        if (type == HealthApi.BLE_BIND_FINISHED) {
            if (scanning == false) {
                Log.e(TAG, "scanning");
                mHealthApi.bleScan(true);
                scanning = true;
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OnBLEService.GATT_DEVICE_FOUND_MSG:
                    Bundle data = msg.getData();
                    final BluetoothDevice device = data.getParcelable(BluetoothDevice.EXTRA_DEVICE);
                    final int rssi = data.getInt(OnBLEService.EXTRA_RSSI);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addDevice(device, rssi);
                        }
                    });
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        setContentView(R.layout.devicelist);
        populateList();


        mHealthApi = new HealthApi();
        mHealthApi.initBleScan(this, mHandler);
        mHealthApi.setBleCallBack(this);


        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        mEmptyList = (TextView) findViewById(R.id.empty);
        Button cancelButton = (Button) findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("", "times is " + time);
                if (scanning) {
                    mHealthApi.bleScan(false);
                    scanning = false;
                }
                if (timer != null)
                    timer.cancel();
                if (deviceAdapterSort != null)
                    deviceAdapterSort.clear();
                finish();
            }
        });
    }

    private void populateList() {
        /* Initialize device list container */
        Log.d(TAG, "populateList");
        deviceList = new ArrayList<BluetoothDevice>();
        devsorts = new ArrayList<devicesort>();
        deviceAdapter = new DeviceAdapter(this, deviceList);
        deviceAdapterSort = new DeviceAdapterSort(this, devsorts);

        devRssiValues = new HashMap<String, Integer>();

        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
        //newDevicesListView.setAdapter(deviceAdapter);
        newDevicesListView.setAdapter(deviceAdapterSort);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        for (BluetoothDevice pairedDevice : pairedDevices) {
            boolean result = false;
            if (result == true) {
                addDevice(pairedDevice, 0);
            }
        }
    }

    private void addDevice(BluetoothDevice device, int rssi) {
        boolean deviceFound = false;
        for (BluetoothDevice listDev : deviceList) {
            if (listDev.getAddress().equals(device.getAddress())) {
                deviceFound = true;
                break;
            }
        }
        devRssiValues.put(device.getAddress(), rssi);
        for (devicesort d : devsorts) {
            if (d.bleDevice.getAddress().equals(device.getAddress())) {
                d.rssi = rssi;
            }
        }
        if (!deviceFound) {
            mEmptyList.setVisibility(View.GONE);
            devsort = new devicesort();
            devsort.bleDevice = device;
            devsort.rssi = rssi;
            devsorts.add(devsort);
            Collections.sort(devsorts, comparator);

            deviceList.add(device);
            deviceAdapterSort.notifyDataSetChanged();
        } else {
            if (refresh == true) {
                refresh = false;
                time++;
                Log.i(">>>", "refresh times" + time);
                if (timer == null)
                    timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        refresh = true;
                    }
                }, 5000);
                Collections.sort(devsorts, comparator);
                deviceAdapterSort.notifyDataSetChanged();
            }
        }

    }

    private int time = 0;

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        mHealthApi.recoverBleScan();
        if (scanning) {
            mHealthApi.bleScan(false);
            scanning = false;
        }
        super.onDestroy();
    }

    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            devicesort d = devsorts.get(position);

            if (scanning) {
                mHealthApi.bleScan(false);
                scanning = false;
            }

            Bundle b = new Bundle();
            b.putString(BluetoothDevice.EXTRA_DEVICE, devsorts.get(position).bleDevice.getAddress());

            Intent result = new Intent();
            result.putExtras(b);

            setResult(Activity.RESULT_OK, result);
            finish();
        }
    };

    /**
     * The BroadcastReceiver that listens for discovered devices and changes the
     * title when discovery is finished.
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle("选择设备");
                if (deviceList.size() == 0) {
                    mEmptyList.setText("没有设备");
                }
            }
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
                if (!mBtAdapter.isEnabled())
                    finish();
            }
        }
    };

    class DeviceAdapter extends BaseAdapter {
        Context context;
        List<BluetoothDevice> devices;
        LayoutInflater inflater;

        public DeviceAdapter(Context context, List<BluetoothDevice> devices) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.devices = devices;
        }

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public Object getItem(int position) {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGroup vg;

            if (convertView != null) {
                vg = (ViewGroup) convertView;
            } else {
                vg = (ViewGroup) inflater.inflate(R.layout.devicedetail, null);
            }

            BluetoothDevice device = devices.get(position);
            final TextView tvadd = ((TextView) vg.findViewById(R.id.address));
            final TextView tvname = ((TextView) vg.findViewById(R.id.name));
            final TextView tvpaired = (TextView) vg.findViewById(R.id.paired);
            final TextView tvrssi = (TextView) vg.findViewById(R.id.rssi);

            tvrssi.setVisibility(View.VISIBLE);
            byte rssival = (byte) devRssiValues.get(device.getAddress()).intValue();
            if (rssival != 0) {
                tvrssi.setText("Rssi = " + String.valueOf(rssival));
            }

            tvname.setText(device.getName());
            tvadd.setText(device.getAddress());
            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                tvname.setTextColor(Color.GRAY);
                tvadd.setTextColor(Color.GRAY);
                tvpaired.setTextColor(Color.GRAY);
                tvpaired.setVisibility(View.VISIBLE);
                tvpaired.setText("配对");
                tvrssi.setVisibility(View.GONE);
            } else {
                tvname.setTextColor(Color.WHITE);
                tvadd.setTextColor(Color.WHITE);
                tvpaired.setVisibility(View.GONE);
                tvrssi.setVisibility(View.VISIBLE);
                tvrssi.setTextColor(Color.WHITE);
            }
            return vg;
        }
    }

    class DeviceAdapterSort extends BaseAdapter {
        Context context;
        List<devicesort> devices;
        LayoutInflater inflater;

        public DeviceAdapterSort(Context context, List<devicesort> devices) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.devices = devices;
        }

        public void clear() {
            devices.clear();
        }

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public Object getItem(int position) {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGroup vg;

            if (convertView != null) {
                vg = (ViewGroup) convertView;
            } else {
                vg = (ViewGroup) inflater.inflate(R.layout.devicedetail, null);
            }

            BluetoothDevice device = devices.get(position).bleDevice;
            final TextView tvadd = ((TextView) vg.findViewById(R.id.address));
            final TextView tvname = ((TextView) vg.findViewById(R.id.name));
            final TextView tvpaired = (TextView) vg.findViewById(R.id.paired);
            final TextView tvrssi = (TextView) vg.findViewById(R.id.rssi);

            tvrssi.setVisibility(View.VISIBLE);
            byte rssival = (byte) devices.get(position).rssi;
            if (rssival != 0) {
                tvrssi.setText("Rssi = " + String.valueOf(rssival));
            }

            tvname.setText(device.getName());
            tvadd.setText(device.getAddress());
            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                tvname.setTextColor(Color.GRAY);
                tvadd.setTextColor(Color.GRAY);
                tvpaired.setTextColor(Color.GRAY);
                tvpaired.setVisibility(View.VISIBLE);
                tvpaired.setText("配对");
                tvrssi.setVisibility(View.GONE);
            } else {
                tvname.setTextColor(Color.WHITE);
                tvadd.setTextColor(Color.WHITE);
                tvpaired.setVisibility(View.GONE);
                tvrssi.setVisibility(View.VISIBLE);
                tvrssi.setTextColor(Color.WHITE);
            }
            return vg;
        }
    }

}

