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
    public List<Wave> getAllWave(String hrbustID) {
        String s = "wave_" + hrbustID;
        System.out.println(s);
        return waveMapper.getAllWave(hrbustID);
    }

    @Override
    public void insertWave(String hrbustID, Wave wave) {
        waveMapper.insertWave(hrbustID, wave.getTime().toString(), wave.getValue());
    }
}
