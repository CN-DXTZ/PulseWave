package com.db.android.fregment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.db.android.HttpUtils;
import com.db.android.R;
import com.db.android.entity.User;

public class About extends Fragment {
    private Button button_login;
    private Button button_register;
    private EditText textUsername;
    private EditText textPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        textUsername = getActivity().findViewById(R.id.username);
        textPassword = getActivity().findViewById(R.id.password);
        button_login = getActivity().findViewById(R.id.login);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setUsername(textUsername.getText().toString());
                user.setPassword(textPassword.getText().toString());

                new HttpUtils().loginRequest(getActivity(),user);
            }
        });
    }

}
