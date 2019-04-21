package com.db.android;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.db.android.entity.User;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class HttpUtils {
    private String ipv4 = "http://39.107.126.150";

    public void loginRequest(final FragmentActivity fa, User user) {
        OkHttpUtils
                .post()
                .url(ipv4 + "/loginRequest")
                .addParams("username", user.getUsername())
                .addParams("password", user.getPassword())
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonRoot = JSONObject.parseObject(response);
                        if (jsonRoot.getString("success").equals("true")) {
                            Toast.makeText(fa, "登录成功", Toast.LENGTH_SHORT).show();

                            JSONObject jsonUser = jsonRoot.getJSONObject("message");
                            User user = JSONObject.toJavaObject(jsonUser, User.class);
                            System.out.println(user);
                        } else {
                            if (jsonRoot.getString("message").equals("error"))
                                Toast.makeText(fa, "密码错误", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(fa, "账号不存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
