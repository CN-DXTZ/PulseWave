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

import com.db.app.R;
import com.db.app.model.User;
import com.db.app.service.http.LoginHttpUtil;
import com.db.app.service.SharedPreferencesService;

public class LoginActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private CheckBox cb_isRemember;
    private Button bt_login;
    private Button bt_register;

    private Activity mActivity;

    private SharedPreferencesService spService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        initData();
    }

    private void initUI() {
        et_username = this.findViewById(R.id.loginUsername);
        et_password = this.findViewById(R.id.loginPassword);
        cb_isRemember = this.findViewById(R.id.loginIsRemembered);
        bt_login = this.findViewById(R.id.loginLogin);
        bt_register = this.findViewById(R.id.loginRegister);

        bt_login.setOnClickListener(new mLoginOnClickListener());
        bt_register.setOnClickListener(new mRegisterOnClickListener());
        cb_isRemember.setOnCheckedChangeListener(new mOnCheckedChangeListener());
        et_username.addTextChangedListener(mTextWatcher);
        et_password.addTextChangedListener(mTextWatcher);
    }

    // 登录按键
    private class mLoginOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            User loginUser = new User();
            loginUser.setUsername(et_username.getText().toString());
            loginUser.setPassword(et_password.getText().toString());

            LoginHttpUtil.loginRequest(getApplicationContext(), mActivity,
                    loginUser, spService);
        }
    }

    // 注册按键
    private class mRegisterOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            mActivity.finish();
        }
    }

    // 记住账户密码勾选框
    private class mOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                spService.writeLoginConfig(
                        et_username.getText().toString(),
                        et_password.getText().toString(),
                        cb_isRemember.isChecked());
            else
                spService.clearLoginConfig();
        }
    }

    // 文本变化监听实例
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (cb_isRemember.isChecked())
                spService.writeLoginConfig(
                        et_username.getText().toString(),
                        et_password.getText().toString(),
                        cb_isRemember.isChecked());
        }
    };

    private void initData() {
        mActivity = this;
        spService = new SharedPreferencesService(this.getApplicationContext()
                .getSharedPreferences("config", Context.MODE_PRIVATE));

        if (spService.getIsRemember()) {
            et_username.setText(spService.getLgUsername());
            et_password.setText(spService.getLgPassword());
            cb_isRemember.setChecked(true);
        }
    }
}
