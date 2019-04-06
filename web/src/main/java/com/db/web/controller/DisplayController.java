package com.db.web.controller;

import com.db.web.entity.User;
import com.db.web.entity.Wave;
import com.db.web.service.UserService;
import com.db.web.service.WaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class DisplayController {
    @Autowired
    UserService userService;
    @Autowired
    WaveService waveService;

    // 显示数据界面
    @GetMapping(value = "/display")
    public ModelAndView display(ModelAndView modelAndView,
                                @CookieValue(value = "hrbustID") String hrbustID) {
        modelAndView.setViewName("wave");
        User user = userService.selectUser_id(hrbustID);
        modelAndView.addObject("information", user.getUsername() + "波形数据");

        // 获取所有数据并传入 wave.ftl
        List<Wave> waveList = waveService.getAllWave(hrbustID);
        modelAndView.addObject("waveList", waveList);

        return modelAndView;
    }
}
