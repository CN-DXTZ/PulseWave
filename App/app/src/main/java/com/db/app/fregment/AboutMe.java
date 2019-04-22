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


public class AboutMe extends Fragment {
    private EditText etUsername;
    private EditText etPassword;
    private CheckBox cbRemember;
    private Button btLogin;
    private Button btRegister;

    private SharedPreferencesService sharedPreferencesService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.aboutme, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initUI();

        initData();
    }

    private void initUI() {
        etUsername = getActivity().findViewById(R.id.username);
        etPassword = getActivity().findViewById(R.id.password);
        cbRemember = getActivity().findViewById(R.id.isRemember);
        btLogin = getActivity().findViewById(R.id.login);
        btRegister = getActivity().findViewById(R.id.register);

        // 账户密码输入框
        etUsername.addTextChangedListener(mTextWatcher);
        etPassword.addTextChangedListener(mTextWatcher);

        // 记住密码框
        cbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    sharedPreferencesService.writeLoginConfig(
                            etUsername.getText().toString(),
                            etPassword.getText().toString(),
                            cbRemember.isChecked());
                else
                    sharedPreferencesService.clearLoginConfig();
            }
        });

        // 登录按键
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User loginUser = new User();
                loginUser.setUsername(etUsername.getText().toString());
                loginUser.setPassword(etPassword.getText().toString());

                LoginService.loginRequest(getActivity(), loginUser, sharedPreferencesService);
            }
        });

        /**
         * ******************注册按键
         */
        btRegister.setOnClickListener(new View.OnClickListener() {
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
            if (cbRemember.isChecked())
                sharedPreferencesService.writeLoginConfig(
                        etUsername.getText().toString(),
                        etPassword.getText().toString(),
                        cbRemember.isChecked());
        }
    };

    private void initData() {
        sharedPreferencesService = new SharedPreferencesService();
        sharedPreferencesService.setSp(getActivity().getApplicationContext()
                .getSharedPreferences("config", Context.MODE_PRIVATE));

        if (sharedPreferencesService.readIsRemember()) {
            etUsername.setText(sharedPreferencesService.readLgUsername());
            etPassword.setText(sharedPreferencesService.readLgPassword());
            cbRemember.setChecked(true);
        }
    }
}



