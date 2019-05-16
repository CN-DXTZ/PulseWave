package com.db.web.entity;

import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Info {
    private Long time;
    private String wave;
    private String accel;
}
