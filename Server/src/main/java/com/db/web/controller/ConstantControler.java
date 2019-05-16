/*
 * 用户：dengbin
 * 日期：2019/4/12 21:45
 */
package com.db.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.db.web.entity.Wave;
import com.db.web.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ConstantControler {

    @Autowired
    InfoService infoService;

    // 实时请求
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/constantWaveRequest", params = {"id", "prevTime"})
    public String constantWaveRequest(@RequestParam(value = "id") String id,
                                      @RequestParam(value = "prevTime") String prevTime) {
        JSONArray waveJSONArray = new JSONArray();

        List<Wave> waveList = infoService.selectWave_timeAfter(id, prevTime);

        // List<Wave>转JSONArray
        waveJSONArray.addAll(waveList);

        return waveJSONArray.toJSONString();
    }
}
