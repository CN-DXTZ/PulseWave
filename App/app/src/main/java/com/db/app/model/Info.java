package com.db.app.model;

public class Info {
    private Long time;
    private String wave;
    private String accel;

    public Info(Long time, String wave, String accel) {
        this.time = time;
        this.wave = wave;
        this.accel = accel;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getWave() {
        return wave;
    }

    public void setWave(String wave) {
        this.wave = wave;
    }

    public String getAccel() {
        return accel;
    }

    public void setAccel(String accel) {
        this.accel = accel;
    }

    @Override
    public String toString() {
        return "Info{" +
                "time=" + time +
                ", wave='" + wave + '\'' +
                ", accel='" + accel + '\'' +
                '}';
    }
}