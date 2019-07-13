package com.db.app.service.dataService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.db.app.MyApplication;


public class DataService extends Service {
    public final static String TAG_DATASERVICE = "My_DataService";

    public static final int COMMAND_RECEIVE_INIT = 0;
    public static final int COMMAND_RECEIVE_PROCESS = 1;

    public final static String DATA_EXTRA_COMMAND = "com.db.ble.data_extra_command";
    public final static String DATA_RECEIVE_BYTE_ARRAY = "com.db.ble.data_receive_byte_array";

    private ReceiveUtil receiveUtil;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int command = intent.getIntExtra(DATA_EXTRA_COMMAND, -1);
        switch (command) {
            // 初始化接收相关变量 指令
            case COMMAND_RECEIVE_INIT:
                Log.d(TAG_DATASERVICE, "初始化接收相关变量");

                receiveInit();
                break;

            // 处理接收的字节数组 指令
            case COMMAND_RECEIVE_PROCESS:
//                Log.d(TAG_DATASERVICE, "处理接收的字节数组");

                receiveUtil.ReceiveProcess(intent);
                break;

            default:
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化接收相关变量
     */
    private void receiveInit() {
        receiveUtil = new ReceiveUtil((MyApplication) getApplication());
    }


//    /**
//     * 对接收到的单字节数据进行解析为真实数据
//     */
//    private void ReceiveProcess(byte[] receive) {
////        Log.d(TAG_DATASERVICE, "Array-----------: " + Arrays.toString(receive));
//
//        // 处理接收到的数据
//        if (receive != null && receive.length > 0) {
//            int wave;
//            if (firstReceiveTemp == FIRST_PACKET_FLAG) { // 判断为完整数据包中的第一组
//                if (receive[0] == -1) { // 验证数据头  (-1即0xFF)
//                    // 去除掉最初暗光时的无效数据
//                    if (errorFlag) {
//                        wave = toWave(receive[1], receive[2], receive[3]);
//                        if (wave > 0) // 有效
//                            errorFlag = false;
//                        else
//                            return;
//                    }
//                    // 6组脉搏波
//                    for (int i = 1; i < 19; i += 3) {
//                        waveArray.set(numWaveArray++,
//                                toWave(receive[i], receive[i + 1], receive[i + 2]));
//                    }
//                    // 缓存第20个数据，等待第二个分数据包一起解析
//                    firstReceiveTemp = receive[19];
//                }
//            } else { // 完整数据包中的第二组
//                // 3组脉搏波
//                waveArray.set(numWaveArray++,
//                        toWave(firstReceiveTemp, receive[0], receive[1]));
//                for (int i = 2; i < 8; i += 3) {
//                    waveArray.set(numWaveArray++,
//                            toWave(receive[i], receive[i + 1], receive[i + 2]));
//                }
//                // 6组加速度
//                for (int i = 8; i < 20; i += 2) {
//                    accelArray.set(numAccelArray++,
//                            toAccel(receive[i], receive[i + 1]));
//                }
//                // 状态改变
//                firstReceiveTemp = -1;
//            }
//        }
//
//        // 对解析后的数据进行分配
////        receiveDistribution();
//    }

//    // 对解析后的数据进行分配
//    private void receiveDistribution() {
//        if (numAccelArray == MAX_NUM_ACCEL_ARRAY) { // 10个完整蓝牙数据包分配1次
//
//            // 存储进全局变量
//            Info currInfo = new Info(new Date().getTime(), waveArray.toString(), accelArray.toString());
//            MyApplication myApplication = ((MyApplication) (getApplication()));
//            myApplication.setCurrInfo(currInfo);
//            Log.d(TAG_DATASERVICE, "全局currInfo" + currInfo.toString());
//
//            // 重置状态计数
//            numWaveArray = 0;
//            numAccelArray = 0;
//
//            // 存储进数据库
//            sqliteService.insert(myApplication);
//            // 上传至服务器
//            HTTPService.upload(myApplication);
//        }
//    }

//    // 将拆分的三个字节（3个unsigned char）还原为无符号24位Wave数据
//    private int toWave(byte a, byte b, byte c) {
//        int wave = ((toU8(a) << 16) | (toU8(b) << 8) | (toU8(c)));
//        // <=> ((toU8(a) << 16) + (toU8(b) << 8) + (toU8(c)))
//        return (wave - 1000000); // 有效数据都为1000000多，去掉1可以少存储一位
//    }
//
//    // 将拆分的两个字节（第一个为char，第二个为unsigned char）还原为有符号16位Accel数据
//    private int toAccel(byte a, byte b) {
//        return ((a << 8) | (b & 0xff));
//    }
//
//    // 将byte转化为unsigned char
//    private int toU8(byte a) {
//        return a & 0xFF;
//    }
}
