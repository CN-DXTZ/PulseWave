package com.db.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.db.web.entity.User;
import com.db.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@RestController
public class LoginController {

    @Autowired
    UserService userService;

    // 登录请求
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/loginRequest")
    public String loginRequest(User user, HttpServletResponse response) {
        JSONObject back = new JSONObject();

        int state = userService.login(user);
        switch (state) {
            case 0: { // 登录成功
                User curr_user = userService.selectUser_username(user);
                back.put("success", "true");
                back.put("message", JSON.toJSONString(curr_user));

                // 添加Cookie
//                response.addCookie(new Cookie("hrbustID", curr_user.getId().toString()));
//                response.addCookie(new Cookie("hrbustIdentity", ((curr_user.getIdentity()) ? "1" : "0")));
                break;
            }
            case 1: { // 密码不正确
                back.put("success", "false");
                back.put("message", "error");
                break;
            }
            case 2: { // 用户不存在
                back.put("success", "false");
                back.put("message", "nonexistence");
                break;
            }
        }

        return back.toString();
    }
}
