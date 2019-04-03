package com.db.web.controller;

import com.db.web.entity.User;
import com.db.web.entity.Wave;
import com.db.web.service.WaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UpLoadController {

    // 上传界面
    @GetMapping(value = "/upload")
    public String upload() {
        return "/upload.html";
    }

    @Autowired
    WaveService waveService;

    // 登录请求
    @RequestMapping(value = "/uploadRequest")
    public ModelAndView uploadRequest(Wave wave,
                                      ModelAndView modelAndView,
                                      @CookieValue(value = "hrbustID") String hrbustID) {
        modelAndView.setViewName("message");

        waveService.insertWave(hrbustID, wave);

        modelAndView.addObject("information", "上传成功");
        modelAndView.addObject("target", "返回上传");
        modelAndView.addObject("target_url", "upload");

        return modelAndView;
    }
}
