package com.db.app.service.httpService;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.MediaType;

public class UploadHttpUtil {

    public static void uploadRequest(String requestJson) {
//        Log.d(HTTPService.TAG_HTTPSERVICE, requestJson);

        OkHttpUtils
                .postString()
                .url(HTTPService.BASE_URL + "/uploadRequest")
                .content(requestJson)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonRoot = JSONObject.parseObject(response);

//                        if (jsonRoot.getString("success").equals("true")) {
//                        } else {
//                        }

//                        Log.d(HTTPService.TAG_HTTPSERVICE, jsonRoot.toString());
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }
                });
    }
}
