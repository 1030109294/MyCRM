package com.changsan.workbench.service.impl;


import com.changsan.settings.dao.UserDao;
import com.changsan.settings.domain.User;
import com.changsan.workbench.dao.ActivityDao;
import com.changsan.workbench.dao.ActivityRemarkDao;
import com.changsan.workbench.domain.Activity;
import com.changsan.workbench.domain.ActivityRemark;
import com.changsan.workbench.service.ActivityService;
import com.changsan.workbench.vo.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 17925
 * @date 2020/11/17 14:57
 */
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private ActivityRemarkDao activityRemarkDao;

    @Override
    public int addActivity(Activity activity) {

        return  activityDao.insertActivity(activity);

    }

    @Override
    public PaginationVo<Activity> pageList(Map<String , Object> map) {
        PaginationVo<Activity>  paginationVo =  new PaginationVo<>();
       int total =  activityDao.totalActivity(map);
        System.out.println(map.get("skipCount"));
        System.out.println(map.get("pageSize"));
     List<Activity> activityList =  activityDao.selectAllActivity(map);
     paginationVo.setTotal(total);
     paginationVo.setTotalList(activityList);
        return paginationVo;
    }

    @Override
    public boolean delete(String[] id) {
        boolean flag = true;
          //查询remark中需要删除的id有多少记录
        int count1 = activityRemarkDao.getCountByAid(id);
        //删除了remark中多少条记录
        int count2 = activityRemarkDao.deleteRemarkByAid(id);
        //判断存在的记录跟删除的数量是否相等
        if (count1 != count2){
            flag = false;
        }
        //删除activity中所对应id的记录
        int count3 = activityDao.deleteActivityByAid(id);
        //判断删除的记录于需要删除的数量是否相同
        if (count3 != id.length){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getActivityAndUserList(String id) {
        Map<String , Object> map = new HashMap<>();
            List<User> uList =   userDao.selectAllUser();
            Activity a = activityDao.getActivity(id);
            map.put("uList" , uList);
            map.put("a" , a);
        return map;
    }

    @Override
    public int updateActivity(Activity activity) {
      return activityDao.updateActivity(activity);
    }

    @Override
    public Activity getActivityById(String id) {
       return activityDao.detail(id);
    }

    @Override
    public List<ActivityRemark> getRemarkListById(String id) {
      return   activityRemarkDao.getRemarkListById(id);
    }

    @Override
    public boolean deleteRemarkById(String id) {
        int result =activityRemarkDao.deleteRemark(id);
        if (result > 0){
            return true;
        }else {
            return false;
        }

    }

    @Override
    public boolean saveRemark(ActivityRemark activityRemark) {
       int result =  activityRemarkDao.saveRemark(activityRemark);
       if (result != 1){
           return false;
       }else {
           return true;
       }
    }

    @Override
    public ActivityRemark getActivityRemarkById(String id) {
        return  activityRemarkDao.getActivityRemarkById(id);
    }

    @Override
    public boolean updateRemark(ActivityRemark activityRemark) {
        boolean flag = true;
     int result =  activityRemarkDao.updateRemark(activityRemark);
     if (result != 1){
         flag = false;
     }
        return flag;
    }

    @Override
    public List<Activity> getActivityByClueIdAndaName(Map<String, String> map) {
        return activityDao.getActivityByClueIdAndaName(map);
    }

    @Override
    public List<Activity> getActivityByAname(String aname) {
        return activityDao.getActivityByName(aname);
    }
}
