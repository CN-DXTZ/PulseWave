package com.db.web.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private Integer id;
    private String username;
    private String password;
    private Integer age;
    private Boolean identity; // 0为用户 1为医师
}


