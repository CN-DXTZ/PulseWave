package com.db.app.service;

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

import java.util.LinkedList;
import java.util.Queue;
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

    private Toast mToast;
    private Handler mHandler;
    private Context mContext;

    public final static String TAG_BLEGATTSERVICE = "BLEGattService";

    public static final int COMMAND_CONNECT = 0;
    public static final int COMMAND_DISCONNECT = 1;
    public static final int COMMAND_WRITE_CHARACTERISTIC = 2;
    public static final int COMMAND_COLLECT = 3;
    public static final int COMMAND_DISCOLLECT = 4;

    public final static String EXTRA_COMMAND = "com.db.ble.extra_command";
    public final static String CONNECT_ADDRESS = "com.db.ble.connect_address";
    public final static String WRITE_VALUE = "com.db.ble.write_value";

    public static final String COLLECT_DISABLE = "1"; // 停止采集标志
    public static final String COLLECT_ENABLE = "2"; // 开始采集标志

    public final static UUID UUID_SERVICE = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    public final static UUID UUID_CHARACTERISTIC = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

    private byte firstReceiveTemp; // 存储第一组接收数据中暂时未使用的一个字节
    private int numDataPacket; // 记录数据包个数
    private int numStart; // 跳过最开始采集的两个包
    private Queue<Integer> waveQueue; // 记录脉搏波实际值
    private Queue<Integer> accelQueue; // 记录加速度实际值
    private SqliteService sqliteService;


    @Override
    public IBinder onBind(Intent intent) {
        return null; // 不绑定服务: by startService()，直接返回null即可
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 获取蓝牙管理和适配器
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        mHandler = new Handler();
        mContext = this;

        Log.d(TAG_BLEGATTSERVICE, "服务已启动");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG_BLEGATTSERVICE, "服务已终止");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int command = intent.getIntExtra(EXTRA_COMMAND, -1);
        String connectAddress = null;
        switch (command) {
            // 连接指令
            case COMMAND_CONNECT:
                Log.d(TAG_BLEGATTSERVICE, "连接蓝牙");
                connectAddress = intent.getStringExtra(CONNECT_ADDRESS);
                mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(connectAddress);
                mBluetoothGatt = mBluetoothDevice.connectGatt(this, true, mBluetoothGattCallback);
                discoverService(); // 发现服务
                break;

            // 断开连接指令
            case COMMAND_DISCONNECT:
                Log.d(TAG_BLEGATTSERVICE, "断开蓝牙");
                if (mBluetoothGatt != null)
                    mBluetoothGatt.disconnect();
                break;

            // 发送数据指令（未使用）
            case COMMAND_WRITE_CHARACTERISTIC:
                Log.d(TAG_BLEGATTSERVICE, "发送数据");
                String strWrite = intent.getStringExtra(WRITE_VALUE);
                bleSend(strWrite);
                break;

            // 启动采集指令
            case COMMAND_COLLECT:
                Log.d(TAG_BLEGATTSERVICE, "启动采集");

                // 初始化第一组接收数据中未使用节缓存
                firstReceiveTemp = -1;
                numDataPacket = 0;
                numStart = 4;
                if (waveQueue == null)
                    waveQueue = new LinkedList<Integer>();
                if (accelQueue == null)
                    accelQueue = new LinkedList<Integer>();
                if (sqliteService == null)
                    sqliteService = new SqliteService(mContext);

                bleSend(COLLECT_ENABLE);
                break;

            // 停止采集指令
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
            Log.d(TAG_BLEGATTSERVICE, "获得数据输入");

            receiveProcess(characteristic);
        }

        /**
         * 对接收到的单字节数据进行解析为真实数据
         */
        private void receiveProcess(BluetoothGattCharacteristic characteristic) {

            if ((--numStart) == 0)
                return;

            final byte[] receive = characteristic.getValue();
//            Log.d(TAG_BLEGATTSERVICE, "Array000-----------: " + Arrays.toString(receive));

            // 处理接收到的数据并缓存
            // -1即0xFF
            if (receive != null && receive.length > 0) {
                if (firstReceiveTemp == -1) { // 完整数据包中的第一组
                    if (receive[0] == -1) { // 判断数据头
//                        Log.d(TAG_BLEGATTSERVICE, "Array111: " + Arrays.toString(receive));

                        // 6组脉搏波
                        for (int i = 1; i < 19; i += 3) {
                            waveQueue.add(toWave(receive[i], receive[i + 1], receive[i + 2]));
                        }

                        firstReceiveTemp = receive[19];
                    }
                } else { // 第二组
//                    Log.d(TAG_BLEGATTSERVICE, "Array222: " + Arrays.toString(receive));

                    // 3组脉搏波
                    waveQueue.add(toWave(firstReceiveTemp, receive[0], receive[1]));
                    for (int i = 2; i < 8; i += 3) {
                        waveQueue.add(toWave(receive[i], receive[i + 1], receive[i + 2]));
                    }

                    // 6组加速度
                    for (int i = 8; i < 20; i += 2) {
                        accelQueue.add(toAccel(receive[i], receive[i + 1]));
                    }

                    firstReceiveTemp = -1;
                    numDataPacket++;
                }
            }

            // 对还原后的数据进行分配
            if (numDataPacket == 10) {
                numDataPacket = 0;

                Log.d(TAG_BLEGATTSERVICE, "waveQueue:" + waveQueue.toString());
                Log.d(TAG_BLEGATTSERVICE, "accelQueue:" + accelQueue.toString());

                sqliteService.insert(waveQueue.toString(), waveQueue.toString());
                waveQueue.clear();
                accelQueue.clear();
            }
        }

        // 将拆分的三个字节（3个unsigned char）还原为无符号24位Wave数据
        private int toWave(byte a, byte b, byte c) {
            return ((toU8(a) << 16) | (toU8(b) << 8) | (toU8(c)));
            // <=> ((toU8(a) << 16) + (toU8(b) << 8) + (toU8(c)))
        }

        // 将拆分的两个字节（第一个为char，第二个为unsigned char）还原为有符号16位Accel数据
        private int toAccel(byte a, byte b) {
            return ((a << 8) | (b & 0xff));
        }

        // 将byte转化为unsigned char
        private int toU8(byte a) {
            return a & 0xFF;
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