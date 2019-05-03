package com.db.app;

import android.app.Application;

import com.db.app.model.User;

/**
 * 全局变量
 */
public class MyApplication extends Application {
    User currUser;

    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }
}
