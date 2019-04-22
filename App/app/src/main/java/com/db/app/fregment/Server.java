package com.db.app.fregment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.db.app.R;
import com.db.app.entity.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import okhttp3.Call;
import okhttp3.ResponseBody;


public class Server extends Fragment {
    private Button button_connect_bluetooth;

    private String BASE_URL = "http://39.107.126.150/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.server, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        button_connect_bluetooth = getActivity().findViewById(R.id.connect_server);

        button_connect_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadRequest("4", "6", "9");

            }
        });
    }

    public void downloadRequest(String id, String startTime, String endTime) {
        OkHttpUtils
                .get()
                .url(BASE_URL + "/downloadRequest")
                .addParams("id", id)
                .addParams("startTime", startTime)
                .addParams("endTime", endTime)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonRoot = JSONObject.parseObject(response);
                        Log.d("---response---", jsonRoot.toString());
                        Toast.makeText(getActivity(), jsonRoot.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }
                });
    }

}


