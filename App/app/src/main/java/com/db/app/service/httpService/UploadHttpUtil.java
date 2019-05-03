package com.db.app.service.httpService;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.MediaType;

public class UploadHttpUtil {
    public static String BASE_URL = "http://39.107.126.150/";

    public static void uploadRequest(Context context, Activity activity,
                                     String requestJson) {
        OkHttpUtils
                .postString()
                .url(BASE_URL + "/uploadRequest")
                .content(requestJson)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonRoot = JSONObject.parseObject(response);

                        if (jsonRoot.getString("success").equals("true")) {
                        } else {
                        }

                        System.out.println("---------------uploadRequest---------------" + jsonRoot.toString());
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }
                });
    }
}
