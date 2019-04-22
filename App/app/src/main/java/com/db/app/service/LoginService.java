package com.db.app.service;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.db.app.entity.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class LoginService {

    public static String BASE_URL = "http://39.107.126.150/";

    public static void loginRequest(final FragmentActivity fa, User user, SharedPreferencesService sharedPreferencesService) {
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
                            Toast.makeText(fa, "登录成功", Toast.LENGTH_SHORT).show();

                            JSONObject jsonUser = jsonRoot.getJSONObject("message");
                            User currUser = JSONObject.toJavaObject(jsonUser, User.class);
                            System.out.println(currUser);
                            sharedPreferencesService.writeUser(currUser);
                        } else {
                            if (jsonRoot.getString("message").equals("error"))
                                Toast.makeText(fa, "密码错误", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(fa, "账号不存在", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }
                });
    }
}
