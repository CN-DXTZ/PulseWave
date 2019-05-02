package com.db.app.fragment.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.db.app.R;
import com.db.app.model.User;
import com.db.app.service.SharedPreferencesService;

import java.util.ArrayList;
import java.util.Arrays;


public class ProfileFragment extends Fragment {
    private ImageView im_photo;
    private ListView lv_profile;
    private Button bt_login;
    private Button bt_register;
    private Button bt_quit;

    private SharedPreferencesService spService;
    private User currUser;
    private Toast mToast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
        initData();
    }


    private void initUI() {
        im_photo = getActivity().findViewById(R.id.profilePhoto);
        lv_profile = getActivity().findViewById(R.id.profileProfile);
        bt_login = getActivity().findViewById(R.id.profileLogin);
        bt_register = getActivity().findViewById(R.id.profileRegister);
        bt_quit = getActivity().findViewById(R.id.profileQuit);

        bt_login.setOnClickListener(new mLoginOnClickListener());
        bt_register.setOnClickListener(new mRegisterOnClickListener());
        bt_quit.setOnClickListener(new mQuitOnClickListener());
    }

    // 登录按键
    private class mLoginOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showToast("登录");
        }
    }

    // 注册按键
    public class mRegisterOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showToast("注册");

        }
    }

    // 注销按键
    private class mQuitOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showToast("注销");
        }
    }

    private void initData() {
        spService = new SharedPreferencesService(getActivity().getApplicationContext()
                .getSharedPreferences("config", Context.MODE_PRIVATE));
        currUser = spService.getUser();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateButton();
        updateListView();
    }

    // 更新按键
    private void updateButton() {
        if (currUser == null) { // 未登录/已注销
            bt_login.setVisibility(Button.VISIBLE);
            bt_register.setVisibility(Button.VISIBLE);
            bt_quit.setVisibility(Button.GONE);
        } else { //已登录
            bt_login.setVisibility(Button.GONE);
            bt_register.setVisibility(Button.GONE);
            bt_quit.setVisibility(Button.VISIBLE);
        }
    }

    // 更新列表
    private void updateListView() {
        // 列表视图绑定适配器
        lv_profile.setAdapter(new ProfileItemAdapter(this.getContext(), userToProfileItemArray(currUser)));
    }

    private ArrayList<ProfileItem> userToProfileItemArray(User user) {
        String name = "", gender = "", age = "", height = "", weight = "", phone = "";
        if (user != null) {
            if (user.getName() != null)
                name = user.getName();
            if (user.getGender() != null) {
                if (user.getGender())
                    gender = "女";
                else
                    gender = "男";
            }
            if (user.getAge() != null)
                age = user.getAge().toString();
            if (user.getHeight() != null)
                height = user.getHeight().toString();
            if (user.getWeight() != null)
                weight = user.getWeight().toString();
            if (user.getPhone() != null)
                phone = user.getPhone();
        }
        ArrayList<ProfileItem> arrayList = new ArrayList<ProfileItem>(Arrays.asList(
                new ProfileItem("姓名", name),
                new ProfileItem("性别", gender),
                new ProfileItem("年龄", age),
                new ProfileItem("身高", height),
                new ProfileItem("体重", weight),
                new ProfileItem("手机", phone)
        ));
        return arrayList;
    }

    private void showToast(final String showText) {
        if (mToast == null)
            mToast = Toast.makeText(getActivity(), showText, Toast.LENGTH_SHORT);
        else
            mToast.setText(showText);
        mToast.show();
    }
}



