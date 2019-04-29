package com.db.app.entity;

public class Wave {
    private Integer time;
    private String value;

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
