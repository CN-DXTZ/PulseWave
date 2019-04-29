package com.db.app.fregment.BlueTooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.db.app.R;
import com.db.app.service.BLEService;

import java.util.ArrayList;


public class BLE extends Fragment {
    private Button bt_bleScan;
    private ListView listView_bleDevices;
    private LinearLayout linearView_BLEManger;
    private boolean mScanning = false;
    private Button bt_bleConnect;
    private boolean mConnecting = false;
    private Button bt_bleCollect;
    private boolean mCollecting = false;
    private Button bt_bleRealTime;

    private BLEService bleService;
    private ArrayList<BluetoothDevice> list_bleDevices;

    private static final long SCAN_PERIOD = 10000; // 扫描时间：10s

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bluetooth, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initUI();
        initData();
    }

    /**
     * UI初始化
     */
    private void initUI() {
        bt_bleScan = getActivity().findViewById(R.id.bleScan);
        listView_bleDevices = getActivity().findViewById(R.id.bleDevices);
        linearView_BLEManger = getActivity().findViewById(R.id.BLEManger);
        bt_bleConnect = getActivity().findViewById(R.id.bleConnect);
        bt_bleCollect = getActivity().findViewById(R.id.bleStart);
        bt_bleRealTime = getActivity().findViewById(R.id.bleRealTime);

        bt_bleScan.setOnClickListener(new mScanOnClickListener());
        listView_bleDevices.setOnItemClickListener(new mOnItemClickListener());
        bt_bleConnect.setOnClickListener(new mConnectOnClickListener());
        bt_bleCollect.setOnClickListener(new mCollectOnClickListener());
        bt_bleRealTime.setOnClickListener(new mRealTimeOnClickListener());
    }

    /**
     * 蓝牙设备 扫描/停止
     */
    class mScanOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            linearView_BLEManger.setVisibility(View.GONE); // 隐藏 选中蓝牙的管理界面
            connectEnable(false); // 断开连接

            if (mScanning) { // 正在扫描，点击停止
                scanEnable(false);
            } else { // 还未扫描，点击开始
                // 预定义扫描时长——SCAN_PERIOD后自动停止扫描
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mScanning) {
                            scanEnable(false);
                        }
                    }
                }, SCAN_PERIOD);

                scanEnable(true);
            }
        }
    }

    public void scanEnable(final boolean enable) {
        if (enable) { // 启动扫描
            mScanning = true;
            bt_bleScan.setText(R.string.bleDisScanned);

            list_bleDevices.clear(); // 清楚设备列表
            bleService.scanDeviceEnable(true); // 蓝牙服务启动搜索附近设备
        } else { // 停止扫描
            mScanning = false;
            bt_bleScan.setText(R.string.bleScan);

            bleService.scanDeviceEnable(false); // 蓝牙服务停止搜索附近设备
        }
    }


    /**
     * 点击（选中）蓝牙设备列表项
     */
    class mOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //获取当前Device数据并只显示当前Device
            BluetoothDevice currBLEDevice = (BluetoothDevice) parent.getItemAtPosition(position);
            list_bleDevices.clear();
            list_bleDevices.add(currBLEDevice);
            updateListView();

            Toast.makeText(getActivity(), currBLEDevice.getName(), Toast.LENGTH_LONG).show();
            bleService.scanDeviceEnable(false);
            linearView_BLEManger.setVisibility(View.VISIBLE); // 显示 选中蓝牙的管理界面
        }
    }

    /**
     * 蓝牙设备 连接/断开
     */
    class mConnectOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (mConnecting) {
                connectEnable(false);
            } else {
                connectEnable(true);
            }
        }
    }

    public void connectEnable(final boolean enable) {
        if (enable) { // 连接
            mConnecting = true;
            bt_bleConnect.setText(R.string.bleDisConnect);
        } else { // 断开
            mConnecting = false;
            bt_bleConnect.setText(R.string.bleConnect);

            collectEnable(false);
        }
    }

    /**
     * 采集 开始/停止
     */
    class mCollectOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (mConnecting) {
                if (mCollecting) {
                    collectEnable(false);
                } else {
                    collectEnable(true);
                }
            } else {
                Toast.makeText(getActivity(), "蓝牙未连接", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void collectEnable(final boolean enable) {
        if (enable) { // 启动采集
            mCollecting = true;
            bt_bleCollect.setText(R.string.bleDisCollect);
        } else { // 停止采集
            mCollecting = false;
            bt_bleCollect.setText(R.string.bleCollect);
        }
    }

    /**
     * 实时数据
     */
    class mRealTimeOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (mConnecting) {
                Toast.makeText(getActivity(), "实时波形", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "蓝牙未连接", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * 更新ListView
     */
    private void updateListView() {
        // 列表视图绑定适配器
        listView_bleDevices.setAdapter(new BLEDeviceItemAdapter(this.getContext(), list_bleDevices));
    }

    /**
     * Data初始化
     */
    private void initData() {
        list_bleDevices = new ArrayList<BluetoothDevice>();
        bleService = new BLEService(getActivity(), new mLeScanCallback());
    }

    /**
     * BLE服务之扫描回调函数
     */
    class mLeScanCallback implements BluetoothAdapter.LeScanCallback {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, final byte[] scanRecord) {
            // 若蓝牙设备列表未存储该设备地址且设备名不为空
            if (!list_bleDevices.contains(device) && device.getName() != null) {
                list_bleDevices.add(device);
                updateListView();
            }
        }
    }
}


