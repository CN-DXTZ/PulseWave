package com.db.app.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DataService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
