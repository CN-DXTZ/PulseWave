package com.db.app.fregment.BlueTooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.db.app.R;

import java.util.ArrayList;

class BLEDeviceItemAdapter extends ArrayAdapter {
    public BLEDeviceItemAdapter(Context context, ArrayList<BluetoothDevice> bleDevices) {
        // 因为textView为自定义，故不需要布局资源ID，故textViewResourceId随便填
        super(context, 0, bleDevices);
    }

    /**
     * 以上述Adapter输入参数，说明每个Item存储了一个 BluetoothDevice
     * 而本函数则为渲染时，通过对其getName()给对应当前的bleDeviceName
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获取当前Device数据
        BluetoothDevice currBLEDevice = (BluetoothDevice) getItem(position);

        //重复使用的现有列表项视图
        View currItemView = convertView;

        //若不存在，则从指定的layout布局膨胀创建新的列表项视图
        if (currItemView == null) {
            currItemView = LayoutInflater.from(getContext()).inflate(R.layout.bledevice_item, parent, false);
        }

        TextView tv_bleDeviceName = (TextView) currItemView.findViewById(R.id.bleDeviceName);
        tv_bleDeviceName.setText(currBLEDevice.getName());

        return currItemView;
    }
}

