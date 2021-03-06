package com.db.app.fragment.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
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
import com.db.app.service.bluetoothService.BLEGattService;
import com.db.app.service.bluetoothService.BLEScanService;

import java.util.ArrayList;


public class BLEFragment extends Fragment {
    private Button bt_bleScan;
    private ListView listView_bleDevices;
    private LinearLayout linearView_BLEManger;
    private boolean mScanning;
    private Button bt_bleConnect;
    private boolean mConnecting;
    private Button bt_bleCollect;
    private boolean mCollecting;

    private BLEScanService mBLEScanService;
    private static Intent mIntent_BLECtrlService;

    private ArrayList<BluetoothDevice> listBLEDevices;
    private BluetoothDevice mBLEDevice;

    private static final long SCAN_PERIOD = 10000; // 扫描时间：10s


    private Toast mToast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bluetooth, container, false);
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

        bt_bleScan.setOnClickListener(new mScanOnClickListener());
        listView_bleDevices.setOnItemClickListener(new mOnItemClickListener());
        bt_bleConnect.setOnClickListener(new mConnectOnClickListener());
        bt_bleCollect.setOnClickListener(new mCollectOnClickListener());
    }

    /**
     * 蓝牙设备 扫描/停止
     */
    private class mScanOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            linearView_BLEManger.setVisibility(View.GONE); // 隐藏 选中蓝牙的管理界面
            connectEnable(false); // 断开连接
            mBLEDevice = null; // 清楚当前蓝牙设备信息

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

            listBLEDevices.clear(); // 清楚设备列表
            mBLEScanService.scanDeviceEnable(true); // 蓝牙服务启动搜索附近设备
        } else { // 停止扫描
            mScanning = false;
            bt_bleScan.setText(R.string.bleScan);

            mBLEScanService.scanDeviceEnable(false); // 蓝牙服务停止搜索附近设备
        }
    }


    /**
     * 点击（选中）蓝牙设备列表项
     */
    class mOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // 获取当前Device数据
            mBLEDevice = (BluetoothDevice) parent.getItemAtPosition(position);
            // 只显示当前Device
            listBLEDevices.clear();
            listBLEDevices.add(mBLEDevice);
            updateListView();

//            showToast("蓝牙名称:" + mBLEDevice.getName()
//                    + "\n蓝牙MAC地址: " + mBLEDevice.getAddress());

            scanEnable(false);
            linearView_BLEManger.setVisibility(View.VISIBLE); // 显示 选中蓝牙的管理界面
        }
    }

    /**
     * 蓝牙设备 连接/断开
     */
    private class mConnectOnClickListener implements OnClickListener {
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

            // 通过intent告诉蓝牙控制服务连接蓝牙
            mIntent_BLECtrlService.putExtra(BLEGattService.BLE_EXTRA_COMMAND, BLEGattService.COMMAND_CONNECT);
            mIntent_BLECtrlService.putExtra(BLEGattService.BLE_CONNECT_ADDRESS, mBLEDevice.getAddress());
        } else { // 断开
            mConnecting = false;
            bt_bleConnect.setText(R.string.bleConnect);

            collectEnable(false);
            // 通过intent告诉蓝牙控制服务断开蓝牙
            mIntent_BLECtrlService.putExtra(BLEGattService.BLE_EXTRA_COMMAND, BLEGattService.COMMAND_DISCONNECT);
        }
        getActivity().startService(mIntent_BLECtrlService);
    }

    /**
     * 采集 开始/停止
     */
    private class mCollectOnClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (mConnecting) {
                if (mCollecting) {
                    collectEnable(false);
                } else {
                    collectEnable(true);
                }
            } else {
                showToast("蓝牙未连接");
            }
        }
    }

    public void collectEnable(final boolean enable) {
        if (enable) { // 启动采集
            mCollecting = true;
            bt_bleCollect.setText(R.string.bleDisCollect);

            // 通过intent告诉蓝牙控制服务发送开始采集标志
            mIntent_BLECtrlService.putExtra(BLEGattService.BLE_EXTRA_COMMAND, BLEGattService.COMMAND_COLLECT);
        } else { // 停止采集
            mCollecting = false;
            bt_bleCollect.setText(R.string.bleCollect);

            // 通过intent告诉蓝牙控制服务发送停止采集标志
            mIntent_BLECtrlService.putExtra(BLEGattService.BLE_EXTRA_COMMAND, BLEGattService.COMMAND_DISCOLLECT);
        }
        getActivity().startService(mIntent_BLECtrlService);
    }


    /**
     * 更新ListView
     */
    private void updateListView() {
        // 列表视图绑定适配器
        listView_bleDevices.setAdapter(new BLEItemAdapter(this.getContext(), listBLEDevices));
    }

    /**
     * Data初始化
     */
    private void initData() {
        mScanning = false;
        mConnecting = false;
        mCollecting = false;
        mBLEScanService = new BLEScanService(getActivity(), new mLeScanCallback());
        listBLEDevices = new ArrayList<>();
        mBLEDevice = null;
        mIntent_BLECtrlService = new Intent(getActivity(), BLEGattService.class);
    }

    /**
     * BLE扫描服务的扫描回调函数
     */
    class mLeScanCallback implements BluetoothAdapter.LeScanCallback {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, final byte[] scanRecord) {
            // 若蓝牙设备列表未存储该设备地址且设备名不为空
            if (!listBLEDevices.contains(device) && device.getName() != null) {
                listBLEDevices.add(device);
                updateListView();
            }
        }
    }

    private void showToast(final String showText) {
        if (mToast == null)
            mToast = Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT);
        else
            mToast.setText(showText);
        mToast.show();
    }
}