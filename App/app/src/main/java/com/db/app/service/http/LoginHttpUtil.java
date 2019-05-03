package com.db.app.service.http;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.db.app.MyApplication;
import com.db.app.model.User;
import com.db.app.service.SharedPreferencesService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;


public class LoginHttpUtil {
    public static String BASE_URL = "http://39.107.126.150/";

    /**
     * 登录请求
     */
    public static void loginRequest(Context context, Activity activity,
                                    User user,
                                    SharedPreferencesService spService) {
        OkHttpUtils
                .post()
                .url(BASE_URL + "/loginRequest")
                .addParams("username", user.getUsername())
                .addParams("password", user.getPassword())
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonRoot = JSONObject.parseObject(response);
                        if (jsonRoot.getString("success").equals("true")) {
                            Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();

                            JSONObject jsonUser = jsonRoot.getJSONObject("message");
                            User currUser = JSONObject.toJavaObject(jsonUser, User.class);

                            // 存储CurrUser进MyApplication和SharedPreferences
                            ((MyApplication) (activity.getApplication())).setCurrUser(currUser);
                            spService.writeCurrUser(currUser);

                            activity.finish();
                        } else {
                            if (jsonRoot.getString("message").equals("error"))
                                Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(context, "账号不存在", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }
                });
    }
}
