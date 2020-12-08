package com.changsan.workbench.dao;

import com.changsan.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByAid(String[] id);

    int deleteRemarkByAid(String[] id);

    List<ActivityRemark> getRemarkListById(String id);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark activityRemark);

    ActivityRemark getActivityRemarkById(String id);

    int updateRemark(ActivityRemark activityRemark);
}
