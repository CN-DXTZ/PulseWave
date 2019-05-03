package com.db.app.model;

public class Wave {
    private Integer time;
    private String value;

    public Wave(Integer time, String value) {
        this.time = time;
        this.value = value;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Wave{" +
                "time=" + time +
                ", value='" + value + '\'' +
                '}';
    }
}
