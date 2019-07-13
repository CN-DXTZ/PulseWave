package com.db.app.service.bluetoothService;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.db.app.service.dataService.DataService;

import java.util.UUID;

/**
 * 蓝牙管理（操作）服务
 */
public class BLEGattService extends Service {
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattService mGattService;
    private BluetoothGattCharacteristic mGattCharacteristic;

    private static Intent mIntent_DataService;

    private Toast mToast;
    private Handler mHandler;

    public final static String TAG_BLEGATTSERVICE = "My_BLEGattService";

    public static final int COMMAND_CONNECT = 0;
    public static final int COMMAND_DISCONNECT = 1;
    public static final int COMMAND_WRITE_CHARACTERISTIC = 2;
    public static final int COMMAND_COLLECT = 3;
    public static final int COMMAND_DISCOLLECT = 4;

    public final static String BLE_EXTRA_COMMAND = "com.db.ble.ble_extra_command";
    public final static String BLE_CONNECT_ADDRESS = "com.db.ble.ble_connect_address";
    public final static String BLE_WRITE_VALUE = "com.db.ble.ble_write_value";

    public static final String COLLECT_DISABLE = "1"; // 停止采集标志
    public static final String COLLECT_ENABLE = "2"; // 开始采集标志

    public final static UUID UUID_SERVICE = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");


    @Override
    public IBinder onBind(Intent intent) {
        return null; // 不绑定服务: by startService()，直接返回null即可
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mIntent_DataService = new Intent(getBaseContext(), DataService.class);

        // 获取蓝牙管理和适配器
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        mHandler = new Handler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int command = intent.getIntExtra(BLE_EXTRA_COMMAND, -1);
        switch (command) {
            // 连接蓝牙 指令
            case COMMAND_CONNECT:
                Log.d(TAG_BLEGATTSERVICE, "连接蓝牙");
                String connectAddress = intent.getStringExtra(BLE_CONNECT_ADDRESS);
                mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(connectAddress);
                mBluetoothGatt = mBluetoothDevice.connectGatt(this, true, mBluetoothGattCallback);
                discoverService(); // 发现服务
                break;

            // 断开连接蓝牙 指令
            case COMMAND_DISCONNECT:
                Log.d(TAG_BLEGATTSERVICE, "断开蓝牙");
                if (mBluetoothGatt != null)
                    mBluetoothGatt.disconnect();
                break;

            // 发送数据 指令 （未使用）
            case COMMAND_WRITE_CHARACTERISTIC:
                Log.d(TAG_BLEGATTSERVICE, "发送数据");
                String strWrite = intent.getStringExtra(BLE_WRITE_VALUE);
                bleSend(strWrite);
                break;

            // 启动采集 指令
            case COMMAND_COLLECT:
                Log.d(TAG_BLEGATTSERVICE, "启动采集");
                mIntent_DataService.putExtra(DataService.DATA_EXTRA_COMMAND,
                        DataService.COMMAND_RECEIVE_INIT); // 发给DataService命令：初始化接收相关变量
                startService(mIntent_DataService);
                bleSend(COLLECT_ENABLE);
                break;

            // 停止采集 指令
            case COMMAND_DISCOLLECT:
                Log.d(TAG_BLEGATTSERVICE, "停止采集");
                bleSend(COLLECT_DISABLE);
                break;

            default:
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void discoverService() {
        /**
         * 错误描述： onServicesDiscovered 不被调用
         * 错误原因：未知，貌似是因为寻找服务时应确保在UI线程调用
         * 解决办法，子线程延时600ms（原理未知）
         * 参考：https://stackoverflow.com/questions/41434555/onservicesdiscovered-never-called-while-connecting-to-gatt-server#comment70285228_41526267
         */
        try {
            Thread.sleep(500);
            mBluetoothGatt.discoverServices();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void bleSend(String strSend) {
        if (mBluetoothGatt != null && mGattCharacteristic != null) {
            mGattCharacteristic.setValue(strSend);
            mBluetoothGatt.writeCharacteristic(mGattCharacteristic);
        }
    }


    private final BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    Log.d(TAG_BLEGATTSERVICE, "蓝牙连接成功");
                    showToast("蓝牙连接成功");
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d(TAG_BLEGATTSERVICE, "蓝牙断开成功");
                    showToast("蓝牙已断开");
                }
            }
        }


        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.d(TAG_BLEGATTSERVICE, "服务和特征获得成功");

            if (status == BluetoothGatt.GATT_SUCCESS) {
                mGattService = mBluetoothGatt.getService(UUID_SERVICE);
                mGattCharacteristic = mGattService.getCharacteristic(UUID_CHARACTERISTIC);

                // 发现服务和特征后，设置特征监听
                setCharacteristicNotification(mGattCharacteristic, true);
            }
        }

        // 设置特征监听
        private void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                                   boolean enabled) {
            mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID_CHARACTERISTIC);
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.d(TAG_BLEGATTSERVICE, "数据发送成功");

            if (status == BluetoothGatt.GATT_SUCCESS) {
                switch (characteristic.getStringValue(0)) {
                    case COLLECT_ENABLE:
                        showToast("启动采集");
                        break;
                    case COLLECT_DISABLE:
                        showToast("停止采集");
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
//            Log.d(TAG_BLEGATTSERVICE, "获得数据输入");

            final byte[] receive = characteristic.getValue();
            mIntent_DataService.putExtra(DataService.DATA_EXTRA_COMMAND,
                    DataService.COMMAND_RECEIVE_PROCESS); // 发给DataService命令：对接收的字节数组进行处理
            mIntent_DataService.putExtra(DataService.DATA_RECEIVE_BYTE_ARRAY,
                    receive);  // 发给DataService接收的字节数组
            startService(mIntent_DataService);
        }


    };

    private void showToast(final String showText) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null)
                    mToast = Toast.makeText(BLEGattService.this, showText, Toast.LENGTH_SHORT);
                else
                    mToast.setText(showText);
                mToast.show();
            }
        });
    }
}