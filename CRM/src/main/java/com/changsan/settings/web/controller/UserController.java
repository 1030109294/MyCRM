package com.changsan.settings.web.controller;

import com.changsan.settings.domain.User;
import com.changsan.settings.service.UserService;
import com.changsan.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 17925
 * @date 2020/11/15 12:04
 */
@Controller
public class UserController {
 @Autowired
  private UserService userService;

  @RequestMapping("login.do")
  @ResponseBody
    public Map loginUser(String loginAct , String loginPwd , HttpServletRequest request){
      Map<String , Object> map = new HashMap<>();
       loginPwd = MD5Util.getMD5(loginPwd);
    String addr = request.getRemoteAddr();
    try {
      User user = userService.queryUserByOne(loginAct, loginPwd, addr);
      HttpSession session = request.getSession();
      session.setAttribute("user" , user);
     map.put("success" , true);
    } catch (Exception e) {
      String msg = e.getMessage();
      map.put("success" , false);
     map.put("msg" , msg);
    }
    return map;
  }
}
