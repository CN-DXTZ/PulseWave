package com.db.app.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.db.app.activity.LoginActivity;
import com.db.app.activity.RegisterActivity;
import com.db.app.model.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;


public class HttpService {

    public static String BASE_URL = "http://39.107.126.150/";

    /**
     * 登录请求
     */
    public static void loginRequest(final Context context, User user,
                                    SharedPreferencesService spService, Activity activity) {
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

                            // 存储当前用户信息
                            spService.writeUser(currUser);

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

    /**
     * 注册请求
     */
    public static void registerRequest(final Context context, User user,
                                       SharedPreferencesService spService, Activity activity) {
        OkHttpUtils
                .post()
                .url(BASE_URL + "/registerRequest")
                .addParams("username", user.getUsername())
                .addParams("password", user.getPassword())
                .addParams("name", user.getName())
                .addParams("gender", user.getGender().toString())
                .addParams("age", user.getAge().toString())
                .addParams("height", user.getHeight().toString())
                .addParams("weight", user.getWeight().toString())
                .addParams("phone", user.getPhone())
                .addParams("identity", user.getIdentity().toString())
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonRoot = JSONObject.parseObject(response);
                        if (jsonRoot.getString("success").equals("true")) {
                            Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();

                            // 存储为登录信息便于注册成功后快速登陆
                            spService.writeLoginConfig(
                                    user.getUsername(),
                                    user.getPassword(),
                                    true);

                            Intent intent = new Intent();
                            intent.setClass(activity, LoginActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                        } else {
                            Toast.makeText(context, "账号已存在", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }
                });
    }
}
