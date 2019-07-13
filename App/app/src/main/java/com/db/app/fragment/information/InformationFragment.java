package com.db.app.fragment.information;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.app.R;
import com.db.app.view.EcgView;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;


public class InformationFragment extends Fragment {

    private ArrayList<Integer> datas = new ArrayList<Integer>();

    private Queue<Integer> data0Q = new LinkedList<Integer>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadDatas();
        simulator();
    }


    /**
     * 模拟心电发送，心电数据是一秒500个包，所以
     */
    private void simulator(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(EcgView.isRunning){
                    if(data0Q.size() > 0){
                        EcgView.addEcgData0(data0Q.poll());
                    }
                }
            }
        }, 0, 2);
    }

    private void loadDatas(){
        try{
            String data0 = "";
            InputStream in = getResources().openRawResource(R.raw.ecgdata);
            int length = in.available();
            byte [] buffer = new byte[length];
            in.read(buffer);
            data0 = new String(buffer);
            in.close();
            String[] data0s = data0.split(",");
            for(String str : data0s){
                datas.add(Integer.parseInt(str));
            }

            data0Q.addAll(datas);
        }catch (Exception e){}

    }

//    private Button bt_upload;
//    private Button bt_download;
//    private CurvePathView cpv;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_information, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        initUI();
//        initData();
//    }
//
//    private void initUI() {
//        bt_upload = getActivity().findViewById(R.id.upload);
//        bt_download = getActivity().findViewById(R.id.download);
//        cpv = getActivity().findViewById(R.id.cpv);
//
//        bt_download.setOnClickListener(new mOnClickListener());
//
//    }
//
//    private class mOnClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View v) {
//
//            Path newPath = new Path();
//
//            int tmp = 1200;
//            newPath.moveTo(tmp, cpv.mHeight / 2);
//
//            for (int i = 0; i < 1; i++) {
//                newPath.lineTo(tmp + 10, cpv.mHeight / 2 - cpv.mHeight / 4);
//                newPath.lineTo(tmp + 35, cpv.mHeight / 2 + cpv.mHeight / 4);
//                newPath.lineTo(tmp + 40, cpv.mHeight / 2);
//
//                newPath.lineTo(tmp + 200, cpv.mHeight / 2);
//                tmp = tmp + 100;
//            }
//            cpv.mPath.addPath(newPath);
//        }
//    }
//
//
//    private void initData() {
//    }
}


