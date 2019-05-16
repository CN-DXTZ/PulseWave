package com.db.web.service;

import com.db.web.entity.Info;
import com.db.web.entity.User;
import com.db.web.mapper.InfoMapper;
import com.db.web.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    InfoMapper infoMapper;


    // 插入用户
    private void insertUser(User user) {
        userMapper.insertUser(user);
    }

    // 根据username获得user
    @Override
    public User selectUser_username(User user) {
        return userMapper.selectUser_username(user);
    }

    // 根据id获得user
    @Override
    public User selectUser_id(String id) {
        return userMapper.selectUser_id(id);
    }

    // 登录
    @Override
    public int login(User user) {
        User curr_user = selectUser_username(user);
        if (curr_user == null)
            return 2; // 用户不存在
        else if (curr_user.getPassword().equals(user.getPassword()))
            return 0; // 登录成功
        else
            return 1; // 密码不正确
    }

    // 注册
    @Resource
    JdbcTemplate jdbcTemplate;

    @Override
    public int register(User user) {
        User curr_user = selectUser_username(user);
        if (curr_user == null) { // 用户不存在
            insertUser(user);
            Integer id = selectUser_username(user).getId();
            if (user.getIdentity()) // 创建医师信息表
            {
                /**
                 **********************创建医师信息表
                 */
            } else { // 创建病人波形数据表
                String createSQL = "CREATE TABLE `info_" + id
                        + "` (`time` bigint(20),"
                        + "`wave` varchar(2048),"
                        + "`accel` varchar(2048),"
                        + "PRIMARY KEY (`time`))";
                jdbcTemplate.execute(createSQL);

//                infoMapper.insertInfo(id.toString(), "0", "", "");
            }
            return 0; // 注册成功
        } else // 用户已存在
            return 1;
    }
}
