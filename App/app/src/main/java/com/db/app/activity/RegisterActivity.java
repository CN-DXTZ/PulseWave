package com.db.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.db.app.R;
import com.db.app.model.User;
import com.db.app.service.HttpService;
import com.db.app.service.SharedPreferencesService;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private EditText et_name;
    private Spinner sp_gender;
    private EditText et_age;
    private EditText et_height;
    private EditText et_weight;
    private EditText et_phone;
    private Spinner sp_identity;
    private Button bt_register;

    private Activity mActivity;

    private SharedPreferencesService spService;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();
        initData();
    }

    private void initUI() {
        et_username = findViewById(R.id.registerUsername);
        et_password = findViewById(R.id.registerPassword);
        et_name = findViewById(R.id.registerName);
        sp_gender = findViewById(R.id.registerGender);
        et_age = findViewById(R.id.registerAge);
        et_height = findViewById(R.id.registerHeight);
        et_weight = findViewById(R.id.registerWeight);
        et_phone = findViewById(R.id.registerPhone);
        sp_identity = findViewById(R.id.registerIdentity);
        bt_register = findViewById(R.id.registerRegister);
        bt_register.setOnClickListener(new mRegisterOnClickListener());
    }

    // 注册按键
    private class mRegisterOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            User registerUser = getRegisterUser();
            if (registerUser != null)
                HttpService.registerRequest(getApplicationContext(), registerUser, spService, mActivity);
        }
    }

    private User getRegisterUser() {
        User registerUser = new User();
        String str;

        str = et_username.getText().toString();
        if (str.isEmpty()) {
            showToast("请输入用户名");
            return null;
        } else
            registerUser.setUsername(str);

        str = et_password.getText().toString();
        if (str.isEmpty()) {
            showToast("请输入密码");
            return null;
        } else
            registerUser.setPassword(str);

        str = et_name.getText().toString();
        if (str.isEmpty()) {
            showToast("请输入姓名");
            return null;
        } else
            registerUser.setName(str);

        str = sp_gender.getSelectedItem().toString();
        if (str.equals("请选择")) {
            showToast("请选择性别");
            return null;
        } else {
            if (str.equals("男"))
                registerUser.setGender(false);
            else if (str.equals("女"))
                registerUser.setGender(true);
        }

        str = et_age.getText().toString();
        if (str.isEmpty()) {
            showToast("请输入年龄");
            return null;
        } else
            registerUser.setAge(Integer.valueOf(str));

        str = et_height.getText().toString();
        if (str.isEmpty()) {
            showToast("请输入身高");
            return null;
        } else
            registerUser.setHeight(Float.valueOf(str));

        str = et_weight.getText().toString();
        if (str.isEmpty()) {
            showToast("请输入体重");
            return null;
        } else
            registerUser.setWeight(Float.valueOf(str));

        str = et_phone.getText().toString();
        if (str.isEmpty()) {
            showToast("请输入手机号");
            return null;
        } else
            registerUser.setPhone(str);

        str = sp_identity.getSelectedItem().toString();
        if (str.equals("请选择")) {
            showToast("请选择身份");
            return null;
        } else {
            if (str.equals("普通用户"))
                registerUser.setIdentity(false);
            else if (str.equals("医师"))
                registerUser.setIdentity(true);
        }
        return registerUser;
    }

    private void initData() {
        mActivity = this;
        spService = new SharedPreferencesService(this.getApplicationContext()
                .getSharedPreferences("config", Context.MODE_PRIVATE));
    }

    private void showToast(final String showText) {
        if (mToast == null)
            mToast = Toast.makeText(this, showText, Toast.LENGTH_SHORT);
        else
            mToast.setText(showText);
        mToast.show();
    }
}
