package com.db.app.service.httpService;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.db.app.MyApplication;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class DownloadHttpUtil {
    private static String BASE_URL = "http://39.107.126.150/";

    public static void downloadRequest(Context context, Activity activity,
                                       String startTime,
                                       String endTime) {
        OkHttpUtils
                .get()
                .url(BASE_URL + "/downloadRequest")
                .addParams("id",
                        ((MyApplication) (activity.getApplication())).getCurrUser().getId().toString())
                .addParams("startTime", startTime)
                .addParams("endTime", endTime)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonRoot = JSONObject.parseObject(response);
                        System.out.println("---------------downloadRequest---------------" + jsonRoot.toString());
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }
                });
    }
}
