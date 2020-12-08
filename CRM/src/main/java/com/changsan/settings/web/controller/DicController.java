package com.changsan.settings.web.controller;

import com.changsan.settings.domain.User;
import com.changsan.settings.service.DicService;
import com.changsan.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author 17925
 * @date 2020/11/21 16:26
 */
@Controller
@RequestMapping("dic")
public class DicController {
    @Autowired
    private DicService dicService;
    @Autowired
    private UserService userService;
    @RequestMapping("getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
       return userService.queryUserAll();
    }
}
