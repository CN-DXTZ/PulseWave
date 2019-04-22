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
    private Boolean gender; // false为男 true位女
    private Integer age;
    private String name;
    private float height;
    private float weight;
    private String phone;
    private Boolean identity; // false为用户 true为医师


}


