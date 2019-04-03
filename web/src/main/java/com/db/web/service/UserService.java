package com.db.web.service;

import com.db.web.entity.User;


public interface UserService {

    User getUser_username(User user);

    User selectUser_id(String hrbustID);

    int login(User user);

    int register(User user);
}
