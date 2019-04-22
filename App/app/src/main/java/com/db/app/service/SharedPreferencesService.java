package com.db.app.service;

import android.content.SharedPreferences;

import com.db.app.entity.User;


public class SharedPreferencesService {
    private static String LG_USERNAME = "lg_username";
    private static String LG_PASSWORD = "lg_password";
    private static String LG_ISREMEMBER = "lg_isRemember";

    private static String US_ID = "us_id";
    private static String US_USERNAME = "us_username";
    private static String US_PASSWORD = "us_password";
    private static String US_AGE = "us_age";
    private static String US_IDENTITY = "us_identity";

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public void setSp(SharedPreferences sp) {
        this.sp = sp;
        this.editor = sp.edit();
    }

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

    public void writeUserConfig(User user) {
        editor.putInt(US_ID, user.getId());
        editor.putString(US_USERNAME, user.getUsername());
        editor.putString(US_PASSWORD, user.getPassword());
        editor.putInt(US_AGE, user.getAge());
        editor.putBoolean(US_IDENTITY, user.getIdentity());
        editor.apply();
    }

    public User readUser() {
        User currUser = new User();
        currUser.setUsername(sp.getString(US_USERNAME, null));
        currUser.setPassword(sp.getString(US_PASSWORD, null));
        currUser.setId(sp.getInt(US_ID, 0));
        currUser.setAge(sp.getInt(US_AGE, 0));
        currUser.setIdentity(sp.getBoolean(US_IDENTITY, false));

        return currUser;
    }
}
