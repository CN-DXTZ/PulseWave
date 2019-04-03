package com.db.web.controller;

import com.db.web.entity.User;
import com.db.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


@Controller
public class LoginController {

    // 登录界面
    @GetMapping(value = {"/login", "/"})
    public String login() {
        return "/login.html";
    }

    @Autowired
    UserService userService;

    // 登录请求
    @RequestMapping(value = "/loginRequest")
    public ModelAndView loginRequest(User user,
                                     ModelAndView modelAndView,
                                     HttpServletResponse response) {
        modelAndView.setViewName("message");
        int state = userService.login(user);
        switch (state) {
            case 0: { // 登录成功
                // 添加Cookie
                User curr_user = userService.getUser_username(user);
                response.addCookie(new Cookie("hrbustID", curr_user.getId().toString()));
                response.addCookie(new Cookie("hrbustIdentity", ((curr_user.getIdentity()) ? "1" : "0")));
                // 消息提醒
                modelAndView.addObject("information", "登录成功");
                modelAndView.addObject("target", "管理");
                modelAndView.addObject("target_url", "manager");
                break;
            }
            case 1: { // 密码不正确
                modelAndView.addObject("information", "密码不正确");
                modelAndView.addObject("target", "返回登录");
                modelAndView.addObject("target_url", "login");
                break;
            }
            case 2: { // 用户不存在
                modelAndView.addObject("information", "用户不存在");
                modelAndView.addObject("target", "返回登录");
                modelAndView.addObject("target_url", "login");
                break;
            }
        }
        return modelAndView;
    }
}
