package com.db.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.db.web.entity.Info;
import com.db.web.entity.Wave;
import com.db.web.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DownloadController {

    @Autowired
    InfoService infoService;

    // 仅下载脉搏波数据请求
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/downloadWaveRequest", params = {"id", "startTime", "endTime"})
    public String downloadWaveRequest(@RequestParam(value = "id") String id,
                                      @RequestParam(value = "startTime") String startTime,
                                      @RequestParam(value = "endTime") String endTime) {
        JSONObject backJson = new JSONObject();
        backJson.put("success", "true");

        List<Wave> waveList = infoService.selectWave_timeRange(id, startTime, endTime);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(waveList);
        backJson.put("wave", jsonArray);

        return backJson.toJSONString();
    }

    // 下载整个信息数据请求
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/downloadInfoRequest", params = {"id", "startTime", "endTime"})
    public String downloadInfoRequest(@RequestParam(value = "id") String id,
                                      @RequestParam(value = "startTime") String startTime,
                                      @RequestParam(value = "endTime") String endTime) {
        JSONObject backJson = new JSONObject();
        backJson.put("success", "true");

        List<Info> infoList = infoService.selectInfo_timeRange(id, startTime, endTime);
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(infoList);
        backJson.put("info", jsonArray);

        return backJson.toJSONString();
    }

    // 获取时间范围请求
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/getTimeRangeRequest", params = {"id"})
    public String getTimeRangeRequest(@RequestParam(value = "id") String id) {
        JSONObject backJson = new JSONObject();
        backJson.put("success", "true");

        JSONObject timeRangeJson = new JSONObject();
        timeRangeJson.put("minTime", infoService.getMinTime(id));
        timeRangeJson.put("maxTime", infoService.getMaxTime(id));
        backJson.put("timeRange", timeRangeJson);

        return backJson.toJSONString();
    }
}