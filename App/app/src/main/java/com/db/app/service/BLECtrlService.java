package com.db.app.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.UUID;

/**
 * 蓝牙控制服务
 */
public class BLECtrlService extends Service {
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattService mBluetoothGattService;
    private BluetoothGattCharacteristic mBluetoothGattCharacteristic;

    private Toast mToast;
    private Handler mHandler;

    public static final int COMMAND_CONNECT = 0;
    public static final int COMMAND_DISCONNECT = 1;
    public static final int COMMAND_SERVICES_DISCOVER = 2;
    public static final int COMMAND_WRITE_CHARACTERISTIC = 3;

    public final static String EXTRA_COMMAND = "com.db.ble.EXTRA_COMMAND";
    public final static String CONNECT_ADDRESS = "com.db.ble.CONNECT_ADDRESS";
    public final static String WRITE_VALUE = "com.db.ble.WRITE_VALUE";

    public final static UUID UUID_SERVICE = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 获取蓝牙管理和适配器
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        mHandler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int command = intent.getIntExtra(EXTRA_COMMAND, -1);
        String connectAddress = null;
        switch (command) {
            case COMMAND_CONNECT:
                connectAddress = intent.getStringExtra(CONNECT_ADDRESS);
                mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(connectAddress);
                mBluetoothGatt = mBluetoothDevice.connectGatt(this, false, mBluetoothGattCallback);
                break;
            case COMMAND_DISCONNECT:
                if (mBluetoothGatt != null)
                    mBluetoothGatt.disconnect();
                break;
            default:
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private final BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    showToast("蓝牙连接成功");
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    showToast("蓝牙已断开");
                }
            }

            super.onConnectionStateChange(gatt, status, newState);
        }
    };

    private void showToast(final String showText) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null)
                    mToast = Toast.makeText(BLECtrlService.this, showText, Toast.LENGTH_SHORT);
                else
                    mToast.setText(showText);
                mToast.show();
            }
        });
    }
}
