/*
 * 用户：dengbin
 * 日期：2019/4/12 21:45
 */
package com.db.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.db.web.entity.Wave;
import com.db.web.service.WaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ConstantControler {

    @Autowired
    WaveService waveService;

    // 实时请求
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/constantRequest", params = {"id", "prevTime"})
    public String constantRequest(@RequestParam(value = "id") String id,
                                  @RequestParam(value = "prevTime") String prevTime) {
        JSONArray waveJSONArray = new JSONArray();

        List<Wave> waveList = waveService.selectWave_timeAfter(id, prevTime);

        // List<Wave>转JSONArray
        waveJSONArray.addAll(waveList);

        return waveJSONArray.toJSONString();
    }
}
