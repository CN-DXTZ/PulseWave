package com.db.web.service;

import com.db.web.entity.Wave;
import com.db.web.mapper.WaveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaveServiceImpl implements WaveService {

    @Autowired
    WaveMapper waveMapper;

    @Override
    public void insertWave(String id, Wave wave) {
        waveMapper.insertWave(id, wave.getTime().toString(), wave.getValue());
    }

    @Override
    public List<Wave> getAllWave(String id) {
        return waveMapper.selectAllWave(id);
    }

    @Override
    public List<Wave> selectWave_timeRange(String id, String startTime, String endTime) {
        return waveMapper.selectWave_timeRange(id, startTime, endTime);
    }

    @Override
    public List<Wave> selectWave_timeAfter(String id, String prevTime) {
        return waveMapper.selectWave_timeAfter(id, prevTime);
    }
}
