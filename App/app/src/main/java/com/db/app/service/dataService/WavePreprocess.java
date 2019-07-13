package com.db.app.service.dataService;

import android.util.Log;

import com.db.app.MyApplication;
import com.db.app.view.EcgView;

import java.util.ArrayList;

public class WavePreprocess {
    private ArrayList<Double> waveArray;
    private ArrayList<LimPoint> limPointArray;
    private int id_ES; // 指数平滑终点索引

    private Double threshold;
    private static Double INIT_THRESHOLD = 5000.0;
    private Integer id_prePair;

    private MyApplication myApplication;

    public WavePreprocess(ArrayList<Double> waveArray, MyApplication myApplication) {
        this.waveArray = waveArray;
        this.limPointArray = new ArrayList<>();
        this.id_ES = 1;
        this.threshold = INIT_THRESHOLD;

        this.myApplication = myApplication;
    }

    public void wavePreprocess() {
        return;
//        Log.d(DataService.TAG_DATASERVICE, "-1-1-1-1-1-1-1");
//        ArrayList<Integer> test = new ArrayList<>(198);
//
//        // 1. 反转并指数平滑
//        if (waveArray.get(0) > 0)
//            waveArray.set(0, -waveArray.get(0));
//
//        for (; id_ES < waveArray.size(); id_ES++) {
//            waveArray.set(id_ES, waveArray.get(id_ES - 1) * 0.75 - waveArray.get(id_ES) * 0.25);
//            test.add(waveArray.get(id_ES).intValue());
//        }
//
//        // 2. 数值极值点
//        int id_limWave;
//        if (limPointArray.isEmpty()) // 如果不存在第一个波
//            id_limWave = 1;
//        else
//            id_limWave = limPointArray.get(limPointArray.size() - 1).index + 1;
//
//        for (; id_limWave < (waveArray.size() - 1); id_limWave++) {
//            // 2:max, -2:min
//            int flag = waveArray.get(id_limWave).compareTo(waveArray.get(id_limWave - 1))
//                    + waveArray.get(id_limWave).compareTo(waveArray.get(id_limWave + 1));
//            // 极值
//            if (flag == 2 || flag == -2)
//                limPointArray.add(new LimPoint(id_limWave, waveArray.get(id_limWave), flag));
//        }
//
//        // 若第一个极值点为最大值，则去除
//        if (!limPointArray.isEmpty() && limPointArray.get(0).flag_isMax > 0)
//            limPointArray.remove(0);
//
//        // 3. 极值点去伪存真（去除抖动）
//        int id_limArray = 1;
//        while (id_limArray < limPointArray.size()) {
//            // 同向极值
//            if (limPointArray.get(id_limArray).flag_isMax == limPointArray.get(id_limArray - 1).flag_isMax) {
//
//                if (limPointArray.get(id_limArray).value > limPointArray.get(id_limArray - 1).value
//                        == limPointArray.get(id_limArray).flag_isMax > 0) // (id)比(id-1)更极限
//                    limPointArray.remove(id_limArray - 1);
//                else
//                    limPointArray.remove(id_limArray);
//            }
//            // 反向抖动，间隔小于10
//            else if (limPointArray.get(id_limArray).index - limPointArray.get(id_limArray - 1).index < 10) {
//                limPointArray.remove(id_limArray);
//            } else {
//                id_limArray++;
//            }
//        }
//
//        int id_nextPair;
//        if (id_prePair == null)
//            id_nextPair = 0;
//        else
//            id_nextPair = 2;
//
//        Log.d(DataService.TAG_DATASERVICE, "00000000000");
//        // 寻找主波峰谷对并分割完整波形
//        while (id_nextPair + 1 < limPointArray.size()) {
//            if (limPointArray.get(id_nextPair + 1).index - limPointArray.get(id_nextPair).index > 100) {
//                id_nextPair += 2;
//                continue;
//            }
//
//            double diff = limPointArray.get(id_nextPair + 1).value - limPointArray.get(id_nextPair).value;
//
//            if (diff > (threshold / 3) && diff < (threshold * 3)) {
//
//                threshold = threshold * 0.75 + diff * 0.25;
//
//                // 没有第一组峰谷对
//                if (id_prePair == null) {
//                    id_prePair = id_nextPair;
//                    id_nextPair += 2;
//                    Log.d(DataService.TAG_DATASERVICE, "111111111");
//                }
//                // 峰谷对无误(20~250)
//                else if (limPointArray.get(id_nextPair).index - limPointArray.get(id_prePair).index > 30 &&
//                        limPointArray.get(id_nextPair).index - limPointArray.get(id_prePair).index < 250) {
//
//                    Log.d(DataService.TAG_DATASERVICE, "222222222");
//                    // 校正基线漂移并均值平滑
//                    int num = id_nextPair - id_prePair;
//                    Double base = limPointArray.get(id_prePair).value;
//                    Double step = (limPointArray.get(id_nextPair).value - base) / num;
//
//                    ArrayList<Integer> splitWave = new ArrayList<>(num);
//                    for (int i = 0; i < num; i++) {
//                        int currPoint;
//                        if (i == 0)
//                            currPoint = 0;
//                        else
//                            currPoint = ((Double)
//                                    ((waveArray.get(id_prePair + i) + i * step - base) * 0.25
//                                            + Double.valueOf(splitWave.get(i - 1)) * 0.75)).intValue();
//
//                        if (EcgView.isRunning) {
//                            EcgView.addEcgData0(currPoint);
//                        }
//                        splitWave.add(currPoint);
//                    }
//
//                    Log.d(DataService.TAG_DATASERVICE, "分割波形");
//                    Log.d(DataService.TAG_DATASERVICE, splitWave.toString());
//
//                    limPointArray.subList(0, id_nextPair).clear();
//                    id_prePair = 0;
//                    id_nextPair = 2;
//                }
//                // 峰谷对有误
//                else {
//                    id_prePair = id_nextPair;
//                    id_nextPair += 2;
//                }
//            }
//
//        }
//
//
//        Log.d(DataService.TAG_DATASERVICE, "原始波形");
//        Log.d(DataService.TAG_DATASERVICE, test.toString());
//
////        HTTPService.upload(myApplication);
    }

    private void delWave(int num) {
        id_ES -= num;
        waveArray.subList(0, num).clear();

        for (int i = 0; i < limPointArray.size(); i++)
            limPointArray.get(i).index -= num;
    }

}


// 极值点
class LimPoint {
    int index;
    double value;
    int flag_isMax;

    LimPoint(int index, double value, int flag_isMax) {
        this.value = value;
        this.index = index;
        this.flag_isMax = flag_isMax;
    }

    @Override
    public String toString() {
        return "LimPoint{" +
                "index=" + index +
                ", value=" + value +
                ", flag_isMax=" + flag_isMax +
                '}';
    }
}

