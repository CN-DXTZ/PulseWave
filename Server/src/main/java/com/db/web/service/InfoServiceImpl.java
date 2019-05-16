package com.db.web.service;

import com.db.web.entity.Info;
import com.db.web.entity.Wave;
import com.db.web.mapper.InfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    InfoMapper infoMapper;

    @Override
    public void insertInfo(String id, Info info) {
        infoMapper.insertInfo(id, info.getTime().toString(), info.getWave(), info.getAccel());
    }

    @Override
    public List<Wave> selectWave_timeRange(String id, String startTime, String endTime) {
        return infoMapper.selectWave_timeRange(id, startTime, endTime);
    }

    @Override
    public List<Wave> selectWave_timeAfter(String id, String prevTime) {
        return infoMapper.selectWave_timeAfter(id, prevTime);
    }

    @Override
    public List<Info> selectInfo_timeRange(String id, String startTime, String endTime) {
        return infoMapper.selectInfo_timeRange(id, startTime, endTime);
    }

    @Override
    public Info selectInfo_time(String id, String currTime) {
        return infoMapper.selectInfo_time(id, currTime);
    }

    @Override
    public Long getMinTime(String id) {
        return infoMapper.selectInfo_minTime(id).getTime();
    }

    @Override
    public Long getMaxTime(String id) {
        return infoMapper.selectInfo_maxTime(id).getTime();
    }
}
