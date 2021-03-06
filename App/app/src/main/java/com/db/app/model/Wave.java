package com.db.app.model;

public class Wave {
    private Long time;
    private String wave;

    public Wave(Long time, String wave) {
        this.time = time;
        this.wave = wave;
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

    @Override
    public String toString() {
        return "Wave{" +
                "time=" + time +
                ", wave='" + wave + '\'' +
                '}';
    }
}