package com.changsan.settings.handler;

import com.changsan.settings.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author 17925
 * @date 2020/11/16 17:56
 */
public class LoginHandler implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("开始进行拦截判断");
       //允许访问登录页面 且 允许第一次账号访问
        String path = request.getServletPath();
      /*  if ("login.jsp".equals(path) || "/login.do".equals(path)){
             return true;
        }else {*/
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user != null){
                return true;
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return false;
        }
    }

