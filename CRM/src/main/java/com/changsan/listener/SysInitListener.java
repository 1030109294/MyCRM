package com.changsan.listener;


import com.changsan.settings.domain.DicValue;
import com.changsan.settings.service.DicService;
import com.changsan.settings.service.impl.DicServiceImpl;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * @author 17925
 * @date 2020/11/21 18:54
 */

public class SysInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //数据字典
        ServletContext application = servletContextEvent.getServletContext();
        DicService dicService = new DicServiceImpl();
      Map<String , List<DicValue>> map =  dicService.getAll(servletContextEvent);
        Set<String> set = map.keySet();
        for (String key : set){
            application.setAttribute(key , map.get(key));
        }
        //================================
        //解析Stage2Possibility.properties文件
        ResourceBundle rs = ResourceBundle.getBundle("Stage2Possibility");
        Map<String , String> Pmap = new HashMap<>();
        Enumeration<String> e = rs.getKeys();
        while (e.hasMoreElements()){
          String key = e.nextElement();
          String value = rs.getString(key);
          Pmap.put(key , value);
        }
        application.setAttribute("Pmap" , Pmap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
