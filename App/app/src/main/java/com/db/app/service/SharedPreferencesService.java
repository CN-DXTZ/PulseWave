package com.db.app.service;

import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.db.app.model.User;


public class SharedPreferencesService {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private static String LOGIN_USERNAME = "login_username";
    private static String LOGIN_PASSWORD = "login_password";
    private static String LOGIN_ISREMEMBER = "login_isRemember";

    private static String USER_JSON = "user_json";

    private static String BLE_ADDRESS = "ble_address";

    public SharedPreferencesService(SharedPreferences sp) {
        this.sp = sp;
        this.editor = sp.edit();
    }

    /**
     * 读写登录信息
     */
    public void writeLoginConfig(String username, String password, Boolean isRemember) {
        editor.putString(LOGIN_USERNAME, username);
        editor.putString(LOGIN_PASSWORD, password);
        editor.putBoolean(LOGIN_ISREMEMBER, isRemember);
        editor.apply();
    }

    public void clearLoginConfig() {
        editor.putBoolean(LOGIN_ISREMEMBER, false);
        editor.apply();
    }

    public String getLgUsername() {
        return sp.getString(LOGIN_USERNAME, "");
    }

    public String getLgPassword() {
        return sp.getString(LOGIN_PASSWORD, "");
    }

    public boolean getIsRemember() {
        return sp.getBoolean(LOGIN_ISREMEMBER, false);
    }


    /**
     * 读写当前用户信息
     */
    public void writeUser(User currUser) {
        editor.putString(USER_JSON, JSON.toJSONString(currUser));
        editor.apply();
    }

    public User getUser() {
        String currUserJson = sp.getString(USER_JSON, null);
        if (currUserJson == null)
            return null;
        return JSON.parseObject(currUserJson, User.class);
    }

    /**
     * 读写蓝牙设备MAC地址信息
     */
    public void writeBLEAddress(String strBLEAddress) {
        editor.putString(BLE_ADDRESS, strBLEAddress);
        editor.apply();
    }

    public String readBLEAddress() {
        String strBLEAddress = sp.getString(BLE_ADDRESS, null);
        if (strBLEAddress == null)
            return null;
        return strBLEAddress;
    }
}
