package com.db.app;

import android.app.Application;

import com.db.app.model.Info;
import com.db.app.model.User;
import com.db.app.model.Wave;

/**
 * 全局变量
 */
public class MyApplication extends Application {
    User currUser;
    Info currInfo;

    public User getCurrUser() {
        return currUser;
    }

    public void setCurrUser(User currUser) {
        this.currUser = currUser;
    }

    public Info getCurrInfo() {
        return currInfo;
    }

    public void setCurrInfo(Info currInfo) {
        this.currInfo = currInfo;
    }
}
