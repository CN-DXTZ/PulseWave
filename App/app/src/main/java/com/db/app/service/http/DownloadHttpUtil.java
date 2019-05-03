package com.db.app.service.http;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.db.app.model.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class DownloadHttpUtil {
    private static String BASE_URL = "http://39.107.126.150/";

    public static void downloadRequest(Context context,
                                       User user,
                                       String startTime,
                                       String endTime) {
        OkHttpUtils
                .get()
                .url(BASE_URL + "/downloadRequest")
                .addParams("id", user.getId().toString())
                .addParams("startTime", startTime)
                .addParams("endTime", endTime)
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonRoot = JSONObject.parseObject(response);
                        Log.d("---response---", jsonRoot.toString());
                        Toast.makeText(context, jsonRoot.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }
                });
    }
}
