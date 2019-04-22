package com.db.app.service;

import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.db.app.entity.User;


public class SharedPreferencesService {
    private static String LG_USERNAME = "lg_username";
    private static String LG_PASSWORD = "lg_password";
    private static String LG_ISREMEMBER = "lg_isRemember";

    private static String USER_JSON = "user_json";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public void setSp(SharedPreferences sp) {
        this.sp = sp;
        this.editor = sp.edit();
    }

    /**
     * 读写登录信息
     */
    public void writeLoginConfig(String username, String password, Boolean isRemember) {
        editor.putString(LG_USERNAME, username);
        editor.putString(LG_PASSWORD, password);
        editor.putBoolean(LG_ISREMEMBER, isRemember);
        editor.apply();
    }

    public void clearLoginConfig() {
        editor.putBoolean(LG_ISREMEMBER, false);
        editor.apply();
    }

    public String readLgUsername() {
        return sp.getString(LG_USERNAME, "");
    }

    public String readLgPassword() {
        return sp.getString(LG_PASSWORD, "");
    }

    public boolean readIsRemember() {
        return sp.getBoolean(LG_ISREMEMBER, false);
    }


    /**
     * 读写当前用户信息
     */
    public void writeUser(User currUser) {
        editor.putString(USER_JSON, JSON.toJSONString(currUser));
        Log.e("writeuser", JSON.toJSONString(currUser));
        editor.apply();
    }

    public User readUser() {
        String user_json = sp.getString(USER_JSON, "");
        if (user_json.isEmpty())
            return null;
        User currUser = JSON.parseObject(user_json, User.class);
        Log.e("readUser", currUser.toString());
        return currUser;
    }
}
