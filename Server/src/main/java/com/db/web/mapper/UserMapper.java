package com.db.web.mapper;

import com.db.web.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE username=#{username}")
    User selectUser_username(User user);

    @Select("SELECT * FROM user WHERE id=${id}")
    User selectUser_id(@Param("id") String id);

    @Insert("INSERT INTO user " +
            "(username,password,gender,age,name,height,weight,phone,identity) VALUES " +
            "(#{username},#{password},#{gender},#{age},#{name},#{height},#{weight},#{phone},#{identity})")
    void insertUser(User user);
}
