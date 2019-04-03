package com.db.web.controller;

import com.db.web.entity.User;
import com.db.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Controller
public class RegisterController {

    // 注册界面
    @GetMapping(value = "/register")
    public String register() {
        return "/register.html";
    }

    @Autowired
    UserService userService;


    // 注册请求
    @RequestMapping(value = "/registerRequest")
    public ModelAndView registerRequest(User user,
                                        ModelAndView modelAndView) {
        modelAndView.setViewName("message");
        int state = userService.register(user);
        switch (state) {
            case 0: { // 注册成功
                modelAndView.addObject("information", "注册成功");
                modelAndView.addObject("target", "返回登录");
                modelAndView.addObject("target_url", "login");
                break;
            }
            case 1: { // 用户已存在
                modelAndView.addObject("information", " 用户已存在");
                modelAndView.addObject("target", "重新注册");
                modelAndView.addObject("target_url", "register");
                break;
            }
        }
        return modelAndView;
    }
}
