package com.db.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.db.web.entity.User;
import com.db.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegisterController {

    @Autowired
    UserService userService;


    // 注册请求
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/registerRequest")
    public String registerRequest(User user) {
        JSONObject back = new JSONObject();

        int state = userService.register(user);
        switch (state) {
            case 0: { // 注册成功
                back.put("success", "true");
                break;
            }
            case 1: { // 用户已存在
                back.put("success", "false");
                break;
            }
        }

        return back.toString();
    }
}
