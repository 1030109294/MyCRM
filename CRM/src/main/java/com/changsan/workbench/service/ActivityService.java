package com.changsan.workbench.service;

import com.changsan.workbench.domain.Activity;
import com.changsan.workbench.domain.ActivityRemark;
import com.changsan.workbench.vo.PaginationVo;

import java.util.List;
import java.util.Map;


public interface ActivityService {

    int addActivity(Activity activity);

    PaginationVo<Activity> pageList(Map<String , Object> map);

    boolean delete(String[] id);

    Map<String, Object> getActivityAndUserList(String id);

    int updateActivity(Activity activity);

    Activity getActivityById(String id);

    List<ActivityRemark> getRemarkListById(String id);

    boolean deleteRemarkById(String id);

    boolean saveRemark(ActivityRemark activityRemark);

    ActivityRemark getActivityRemarkById(String id);

    boolean updateRemark(ActivityRemark activityRemark);

    List<Activity> getActivityByClueIdAndaName(Map<String, String> map);

    List<Activity> getActivityByAname(String aname);
}
