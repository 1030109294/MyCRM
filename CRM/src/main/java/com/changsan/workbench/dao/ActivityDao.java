package com.changsan.workbench.dao;


import com.changsan.workbench.domain.Activity;

import java.util.List;
import java.util.Map;


public interface ActivityDao {

    int insertActivity(Activity activity);

    int totalActivity(Map<String , Object> map);

    List<Activity> selectAllActivity(Map<String , Object> map);

    int deleteActivityByAid(String[] id);

    Activity getActivity(String id);

    int updateActivity(Activity activity);

    Activity detail(String id);

    List<Activity> getActivityByClueId(String id);

    List<Activity> getActivityByClueIdAndaName(Map<String, String> map);

    List<Activity> getActivityByName(String aname);
}
