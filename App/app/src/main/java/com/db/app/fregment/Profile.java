package com.db.app.fregment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.db.app.R;
import com.db.app.entity.User;
import com.db.app.service.LoginService;
import com.db.app.service.SharedPreferencesService;


public class Profile extends Fragment {
    private EditText et_username;
    private EditText et_password;
    private CheckBox cb_isRemember;
    private Button bt_login;
    private Button bt_register;

    private SharedPreferencesService sharedPreferencesService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initUI();

        initData();
    }

    private void initUI() {
        et_username = getActivity().findViewById(R.id.username);
        et_password = getActivity().findViewById(R.id.password);
        cb_isRemember = getActivity().findViewById(R.id.isRemembered);
        bt_login = getActivity().findViewById(R.id.login);
        bt_register = getActivity().findViewById(R.id.register);

        // 账户密码输入框
        et_username.addTextChangedListener(mTextWatcher);
        et_password.addTextChangedListener(mTextWatcher);

        // 记住密码框
        cb_isRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    sharedPreferencesService.writeLoginConfig(
                            et_username.getText().toString(),
                            et_password.getText().toString(),
                            cb_isRemember.isChecked());
                else
                    sharedPreferencesService.clearLoginConfig();
            }
        });

        // 登录按键
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User loginUser = new User();
                loginUser.setUsername(et_username.getText().toString());
                loginUser.setPassword(et_password.getText().toString());

                LoginService.loginRequest(getActivity(), loginUser, sharedPreferencesService);
            }
        });

        /**
         * ******************注册按键
         */
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "注册", Toast.LENGTH_SHORT).show();
            }
        });
    }

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
                sharedPreferencesService.writeLoginConfig(
                        et_username.getText().toString(),
                        et_password.getText().toString(),
                        cb_isRemember.isChecked());
        }
    };

    private void initData() {
        sharedPreferencesService = new SharedPreferencesService(getActivity().getApplicationContext()
                .getSharedPreferences("config", Context.MODE_PRIVATE));

        if (sharedPreferencesService.readIsRemember()) {
            et_username.setText(sharedPreferencesService.readLgUsername());
            et_password.setText(sharedPreferencesService.readLgPassword());
            cb_isRemember.setChecked(true);
        }
    }
}



