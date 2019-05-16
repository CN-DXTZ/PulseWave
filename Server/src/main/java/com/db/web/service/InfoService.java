package com.db.web.service;

import com.db.web.entity.Info;
import com.db.web.entity.Wave;

import java.util.List;

public interface InfoService {

    void insertInfo(String id, Info info);

    List<Wave> selectWave_timeRange(String id, String startTime, String endTime);

    List<Wave> selectWave_timeAfter(String id, String prevTime);

    List<Info> selectInfo_timeRange(String id, String startTime, String endTime);

    Info selectInfo_time(String id, String currTime);

    Long getMinTime(String id);

    Long getMaxTime(String id);
}
