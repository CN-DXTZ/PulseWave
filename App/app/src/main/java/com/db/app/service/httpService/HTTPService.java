package com.db.app.service.httpService;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.db.app.MyApplication;
import com.db.app.model.User;
import com.db.app.service.SharedPreferencesService;

public class HTTPService {
    public final static String BASE_URL = "http://39.107.126.150/";
    public final static String TAG_HTTPSERVICE = "My_HTTPService";


    public static void login(Context context, Activity activity, User user, SharedPreferencesService spService) {
        LoginHttpUtil.loginRequest(context, activity, user, spService);
    }

    public static void register(Context context, Activity activity, User user, SharedPreferencesService spService) {
        RegisterHttpUtil.registerRequest(context, activity, user, spService);
    }

    public static void upload(MyApplication myApplication) {
        JSONObject requestJson = new JSONObject();
        // JSON添加ID
        requestJson.put("id",
                myApplication.getCurrUser().getId().toString());
        // JSON添加Info
        JSONObject infoJson = new JSONObject();
        infoJson.put("time", myApplication.getCurrInfo().getTime());
        infoJson.put("wave", myApplication.getCurrInfo().getWave());
        infoJson.put("accel", "");
        requestJson.put("info", infoJson);

        UploadHttpUtil.uploadRequest(requestJson.toJSONString());
    }

    public static void downloadWave(Application application, String startTime, String endTime) {
        String id = ((MyApplication) (application)).getCurrUser().getId().toString();
        DownloadHttpUtil.downloadWaveRequest(id, startTime, endTime);
    }

}
