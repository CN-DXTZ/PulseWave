package com.db.android.fregment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.db.android.R;

public class Bluetooth extends Fragment {
    private Button button_connect_bluetooth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bluetooth, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        button_connect_bluetooth = getActivity().findViewById(R.id.connect_bluetooth);
        button_connect_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "连接蓝牙", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
