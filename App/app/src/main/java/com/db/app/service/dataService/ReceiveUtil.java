package com.db.app.service.dataService;


import android.app.Application;
import android.content.Intent;

import com.db.app.MyApplication;

import java.util.ArrayList;

public class ReceiveUtil {
    private boolean flag_isFirstPackage;
    private boolean flag_isLowLight;

    private byte[] receiveArray;

    private ArrayList<Double> waveArray; // 存储22个完整数据包的脉搏波值
    private WavePreprocess wavePreprocess;
    private ArrayList<Integer> accelArray;
    private AccelProcess accelProcess;


    public ReceiveUtil(MyApplication myApplication) {
        this.flag_isFirstPackage = true;
        this.flag_isLowLight = true;
        this.receiveArray = new byte[40];
        this.waveArray = new ArrayList<>(198);
        this.wavePreprocess = new WavePreprocess(waveArray, (myApplication));
        this.accelArray = new ArrayList<>(132);
        this.accelProcess = new AccelProcess(accelArray);
    }

    /**
     * 对接收到的数据进行解析为真实数据
     */
    public void ReceiveProcess(Intent intent) {
        byte[] receive = intent.getByteArrayExtra(DataService.DATA_RECEIVE_BYTE_ARRAY);

        // 合并蓝牙数据分包，并解析为真实数据然后进行数据处理
        if (receive != null && receive.length > 0) {

            if (flag_isFirstPackage) { // 两个蓝牙数据分包中的第一组
                if (receive[0] == -1) { // 验证数据头  (-1即0xFF)
                    // 去除掉传感器刚启动时低亮度的无效数据
                    if (flag_isLowLight) {
                        int firstWavePoint = toWave(receive[1], receive[2], receive[3]);

                        if (firstWavePoint > 1000000) // 有效
                            flag_isLowLight = false;

                        return;// 跳过第一组有效值（可能含有无效值）
                    }

                    // 保存第一组数据
                    System.arraycopy(receive, 0, receiveArray, 0, 20);

                    flag_isFirstPackage = false;
                }
            } else { // 两个蓝牙数据分包中的第二组
                flag_isFirstPackage = true;

                // 保存第二组数据
                System.arraycopy(receive, 0, receiveArray, 20, 20);

                // 解析为真实数据
                for (int i = 1; i <= 27; i += 3) // 脉搏波数据
                    waveArray.add((double) toWave(receiveArray[i], receiveArray[i + 1], receiveArray[i + 2]));
                for (int i = 28; i < 40; i += 2) // 加速度数据
                    accelArray.add(toAccel(receiveArray[i], receiveArray[i + 1]));

                // 脉搏波数据预处理
                if (waveArray.size() > 400)
                    wavePreprocess.wavePreprocess();

                // 加速度数据处理
                if (waveArray.size() % 264 == 0)
                    accelProcess.accelProcess();

            }
        }
    }

    // 将拆分的三个字节（3个unsigned char）还原为无符号24位Wave数据
    private int toWave(byte a, byte b, byte c) {
        // <=> ((toU8(a) << 16) + (toU8(b) << 8) + (toU8(c)))
        return ((toU8(a) << 16) | (toU8(b) << 8) | (toU8(c)));
    }

    // 将拆分的两个字节（第一个为char，第二个为unsigned char）还原为有符号16位Accel数据
    private int toAccel(byte a, byte b) {
        return ((a << 8) | (b & 0xff));
    }

    // 将byte转化为unsigned char
    private int toU8(byte a) {
        return a & 0xFF;
    }
}
