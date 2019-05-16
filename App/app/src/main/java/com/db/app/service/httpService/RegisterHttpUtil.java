package com.db.app.service.httpService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.db.app.activity.LoginActivity;
import com.db.app.model.User;
import com.db.app.service.SharedPreferencesService;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 注册请求
 */
public class RegisterHttpUtil {
    public static void registerRequest(Context context, Activity activity,
                                       User user,
                                       SharedPreferencesService spService) {
        // 先添加注册请求必有项
        PostFormBuilder postFormBuilder = OkHttpUtils
                .post()
                .url(HTTPService.BASE_URL + "/registerRequest")
                .addParams("username", user.getUsername())
                .addParams("password", user.getPassword())
                .addParams("identity", user.getIdentity().toString());

        // 再根据条件添加参数
        if (user.getName() != null)
            postFormBuilder.addParams("name", user.getName());
        if (user.getGender() != null)
            postFormBuilder.addParams("gender", user.getGender().toString());
        if (user.getAge() != null)
            postFormBuilder.addParams("age", user.getAge().toString());
        if (user.getHeight() != null)
            postFormBuilder.addParams("height", user.getHeight().toString());
        if (user.getWeight() != null)
            postFormBuilder.addParams("weight", user.getWeight().toString());
        if (user.getPhone() != null)
            postFormBuilder.addParams("phone", user.getPhone());

        // 建立并执行请求
        postFormBuilder
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
