package com.changsan.settings.service.impl;

import com.changsan.settings.dao.DicTypeDao;
import com.changsan.settings.dao.DicValueDao;
import com.changsan.settings.domain.DicType;
import com.changsan.settings.domain.DicValue;
import com.changsan.settings.service.DicService;
import com.changsan.utils.SqlSessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 17925
 * @date 2020/11/21 19:10
 */
@Service
public class DicServiceImpl implements DicService {
   @Autowired
    private DicValueDao dicValueDao;
    @Autowired
    private DicTypeDao dicTypeDao;
    @Override
    public Map<String, List<DicValue>> getAll(ServletContextEvent servletContextEvent) {
        ApplicationContext context =
                WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
//获取bean
      DicTypeDao  typeDao =  context.getBean(DicTypeDao.class);
       DicValueDao valueDao = context.getBean(DicValueDao.class);

        Map<String, List<DicValue>> map = new HashMap<>();
        List<DicType> list =  typeDao.getAllType();
       for (DicType type : list){
           String code = type.getCode();
         List<DicValue> list1 =   valueDao.getDicValueByCode(code);
           map.put(code , list1);
       }
        return map;
    }
}
