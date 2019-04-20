package com.db.web.service;

import com.db.web.entity.User;


public interface UserService {

    // 根据username获得user
    User selectUser_username(User user);

    // 根据id获得user
    User selectUser_id(String id);

    // 登录
    int login(User user);

    // 注册
    int register(User user);
}
