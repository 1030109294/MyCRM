package com.changsan.workbench.web.controller;

import com.changsan.settings.domain.User;
import com.changsan.settings.service.UserService;
import com.changsan.utils.DateTimeUtil;
import com.changsan.utils.UUIDUtil;
import com.changsan.workbench.domain.Activity;
import com.changsan.workbench.domain.ActivityRemark;
import com.changsan.workbench.service.ActivityService;
import com.changsan.workbench.vo.PageVo;
import com.changsan.workbench.vo.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 17925
 * @date 2020/11/17 14:57
 */
@Controller
@RequestMapping("activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserService userService;

    @RequestMapping("queryUserList.do")
    @ResponseBody
    public List queryUserList(){
        List<User> list = userService.queryUserAll();
        return list;
    }

    @RequestMapping("saveActivity.do")
    @ResponseBody
    public boolean saveActivity(Activity activity , HttpServletRequest request){
        String id = UUIDUtil.getUUID();
         String createTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getName();
        activity.setId(id);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        int i = activityService.addActivity(activity);
        if (i > 0){
            return true;
        }
        return false;
    }

    @RequestMapping("pageList.do")
    @ResponseBody
    public PaginationVo<Activity> pageList(PageVo pageVo) {
         Map<String , Object> map = new HashMap<>();
        String pageSizeStr = pageVo.getPageSize();
        String pageNoStr = pageVo.getPageNo();
        int pageNo = Integer.valueOf(pageNoStr);
        int  pageSize = Integer.valueOf(pageSizeStr);
        int skipCount = (pageNo - 1) * pageSize;
        String name = pageVo.getName();
        map.put("name", name);
        map.put("owner", pageVo.getOwner());
        map.put("startDate", pageVo.getStartDate());
        map.put("endDate", pageVo.getEndDate());
        map.put("skipCount" , skipCount);
        map.put("pageSize", pageVo.getPageSize());
         PaginationVo<Activity> paginationVo = activityService.pageList(map);
    return paginationVo;
}
    @RequestMapping("delete.do")
    @ResponseBody
    public boolean delete(String id[]) {
        return activityService.delete(id);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public Map<String , Object> update(String id){
      return   activityService.getActivityAndUserList(id);
    }

    @RequestMapping("updateActivity.do")
    @ResponseBody
    public boolean updateActivity(Activity activity , HttpServletRequest request){
        String editTime1 = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String editBy = user.getName();
        activity.setEditBy(editBy);
        activity.setEditTime(editTime1);
        int i = activityService.updateActivity(activity);
        if (i > 0){
            return true;
        }
        return false;
    }
    @RequestMapping("detail.do")
    public ModelAndView updateActivity(String id){
        ModelAndView modelAndView = new ModelAndView();
       Activity activity =  activityService.getActivityById(id);
       modelAndView.addObject("activity" , activity);
       modelAndView.setViewName("activity/detail");
        return modelAndView;
    }
    @RequestMapping("getRemarkListById.do")
    @ResponseBody
    public List<ActivityRemark> getRemarkListById(String id) {
        return activityService.getRemarkListById(id);
    }
    @RequestMapping("deleteRemarkById.do")
    @ResponseBody
    public boolean deleteRemarkById(String id) {
        id = id.trim();
       return activityService.deleteRemarkById(id);
    }

    @RequestMapping("saveRemark.do")
    @ResponseBody
    public Map saveRemark(ActivityRemark activityRemark , HttpServletRequest request) {
        String uuid = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
      User user = (User) request.getSession().getAttribute("user");
      String createBy = user.getName();
      String editFlag = "0";
      activityRemark.setId(uuid);
      activityRemark.setCreateTime(createTime);
      activityRemark.setCreateBy(createBy);
      activityRemark.setEditFlag(editFlag);
        Map<String , Object> map = new HashMap<>();
      boolean result =   activityService.saveRemark(activityRemark);
      map.put("success" , result);
      map.put("activityRemark" , activityRemark);
        return map;
    }

    @RequestMapping("editRemark.do")
    @ResponseBody
    public ActivityRemark editRemark(String id) {
        id = id.trim();
        ActivityRemark activityRemark = activityService.getActivityRemarkById(id);
        return activityRemark;
    }
    @RequestMapping("updateRemark.do")
    @ResponseBody
    public Map<String , Object> updateRemark(ActivityRemark activityRemark , HttpServletRequest request) {
        String editTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String editBy = user.getName();
        String editFlag = "1";
        activityRemark.setEditFlag(editFlag);
        activityRemark.setEditTime(editTime);
        activityRemark.setEditBy(editBy);
       boolean result =   activityService.updateRemark(activityRemark);
      ActivityRemark activityRemark1 = activityService.getActivityRemarkById(activityRemark.getId());
        Map<String , Object> map = new HashMap<>();
        map.put("success" , result);
        map.put("activityRemark1" , activityRemark1);
        return map;
    }
}
