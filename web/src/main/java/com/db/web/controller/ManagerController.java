package com.db.web.controller;

import com.db.web.entity.User;
import com.db.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class ManagerController {
    @Autowired
    UserService userService;

    @GetMapping(value = "/manager")
    public ModelAndView manager(ModelAndView modelAndView,
                                @CookieValue(value = "hrbustID") String hrbustID,
                                @CookieValue(value = "hrbustIdentity") String hrbustIdentity) {
        modelAndView.setViewName("manager");
        User user = userService.selectUser_id(hrbustID);
        modelAndView.addObject("information", "你好，" + user.getUsername());
        System.out.println(hrbustIdentity);
        List managerList = new ArrayList();
        if (hrbustIdentity.equals("0")) { // 病人
            managerList.add(new HashMap<String, String>() {{
                put("target", "上传数据");
                put("target_url", "upload");
            }});
            managerList.add(new HashMap<String, String>() {{
                put("target", "查看数据");
                put("target_url", "display");
            }});
        } else { // 医师

        }
        modelAndView.addObject("managerList", managerList);
        return modelAndView;
    }
}