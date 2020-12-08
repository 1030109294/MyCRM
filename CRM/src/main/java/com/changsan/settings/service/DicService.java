package com.changsan.settings.service;

import com.changsan.settings.domain.DicType;
import com.changsan.settings.domain.DicValue;

import javax.servlet.ServletContextEvent;
import java.util.List;
import java.util.Map;

public interface DicService {
    Map<String, List<DicValue>> getAll(ServletContextEvent servletContextEvent);
}
