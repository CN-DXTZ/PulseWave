package com.db.web.service;

import com.db.web.entity.Wave;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WaveService {

    void insertWave(String id, Wave wave);

    List<Wave> getAllWave(String id);

    List<Wave> selectWave_timeRange(String id, String startTime, String endTime);

    List<Wave> selectWave_timeAfter(String id, String prevTime);
}
