package com.db.app.service.httpService;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class DownloadHttpUtil {

    public static void downloadWaveRequest(String id,
                                           String startTime,
                                           String endTime) {
        OkHttpUtils
                .get()
                .url(HTTPService.BASE_URL + "/downloadWaveRequest")
                .addParams("id", id)
                .addParams("startTime", startTime)
                .addParams("endTime", endTime)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonRoot = JSONObject.parseObject(response);

//                        if (jsonRoot.getString("success").equals("true")) {
//                        } else {
//                        }

                        Log.d(HTTPService.TAG_HTTPSERVICE, jsonRoot.toString());
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }
                });
    }
}
