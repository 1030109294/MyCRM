package com.changsan.workbench.web.controller;

import com.changsan.settings.domain.User;
import com.changsan.settings.service.UserService;
import com.changsan.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author 17925
 * @date 2020/11/21 16:13
 */
@Controller
@RequestMapping("contacts")
public class ContactsController {
    @Autowired
    private ContactsService contactsService;
    @Autowired
   private UserService userService;
    @RequestMapping("queryUserList.do")
    @ResponseBody
    public List queryUserList(){
        List<User> list = userService.queryUserAll();
        return list;
    }
}
