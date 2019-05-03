package com.db.app.fragment.history;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.db.app.R;
import com.db.app.model.User;
import com.db.app.service.SharedPreferencesService;


public class HistoryFragment extends Fragment {
    private Button bt_upload;
    private Button bt_download;

    private SharedPreferencesService spService;
    private User currUser;

    private Integer clickTimes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
        initData();
    }

    private void initUI() {
        bt_upload = getActivity().findViewById(R.id.upload);
        bt_download = getActivity().findViewById(R.id.download);

        bt_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                DownloadHttpUtil.downloadRequest("4", "6", "9");

            }
        });
    }

    private void initData() {
        spService = new SharedPreferencesService(getActivity().getApplicationContext()
                .getSharedPreferences("config", Context.MODE_PRIVATE));
        currUser = spService.getCurrUser();
        clickTimes = 1;
    }

}


