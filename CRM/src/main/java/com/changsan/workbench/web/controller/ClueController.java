package com.changsan.workbench.web.controller;

import com.changsan.settings.domain.User;
import com.changsan.utils.DateTimeUtil;
import com.changsan.utils.UUIDUtil;
import com.changsan.workbench.domain.Activity;
import com.changsan.workbench.domain.Clue;
import com.changsan.workbench.domain.Customer;
import com.changsan.workbench.domain.Tran;
import com.changsan.workbench.service.ActivityService;
import com.changsan.workbench.service.ClueService;
import com.changsan.workbench.service.TranService;
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
 * @date 2020/11/21 16:12
 */
@Controller
@RequestMapping("clue")
public class ClueController {
    @Autowired
    private ClueService clueService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private TranService tranService;
    @RequestMapping("addClue.do")
    @ResponseBody
    public boolean addClue(Clue clue , HttpServletRequest request){
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getName();
        clue.setId(id);
        clue.setCreateTime(createTime);
        clue.setCreateBy(createBy);
        return  clueService.addClue(clue);
    }
    @RequestMapping("pageList.do")
    @ResponseBody
    public PaginationVo<Clue> pageList(PageVo pageVo) {
        Map<String , Object> map = new HashMap<>();
        String pageSizeStr = pageVo.getPageSize();
        String pageNoStr = pageVo.getPageNo();
        int pageNo = Integer.valueOf(pageNoStr);
        int  pageSize = Integer.valueOf(pageSizeStr);
        int skipCount = (pageNo - 1) * pageSize;
        map.put("fullname" , pageVo.getFullname());
        map.put("company" , pageVo.getCompany());
        map.put("phone" , pageVo.getPhone());
        map.put("mphone" , pageVo.getMphone());
        map.put("source" , pageVo.getSource());
        map.put("state" , pageVo.getStage());
        map.put("owner", pageVo.getOwner());
        map.put("skipCount" , skipCount);
        map.put("pageSize",pageVo.getPageSize());
        PaginationVo<Clue> paginationVo = clueService.pageList(map);
        return paginationVo;
    }
    @RequestMapping("clueDetail.do")
    public ModelAndView clueDetail(String id) {
        ModelAndView modelAndView = new ModelAndView();
        Clue  clue = clueService.getClueById(id);
        modelAndView.addObject("clue" , clue);
        modelAndView.setViewName("clue/detail");
        return modelAndView;
    }
    @RequestMapping("getActivityByClueId.do")
    @ResponseBody
    public List<Activity> getActivityByClueId(String id) {
       return clueService.getActivityByClueId(id);
    }
    @RequestMapping("relatedActivity.do")
    @ResponseBody
    public List<Activity> relatedActivity(String clueId , String activityName) {

        Map<String , String> map = new HashMap<>();
        map.put("clueId" , clueId);
        map.put("activityName" , activityName);
        List<Activity> activityList = activityService.getActivityByClueIdAndaName(map);
        return activityList;
    }
    @RequestMapping("bundActivity.do")
    @ResponseBody
    public boolean bundActivity(String cid , String aid[]) {
        return clueService.addClueActivityRelation(cid , aid);
    }
    @RequestMapping("dismissRelated.do")
    @ResponseBody
    public boolean dismissRelated(String id) {
        return clueService.deleteRelatedById(id);
    }

    @RequestMapping("convert.do")
    public ModelAndView convert(String id) {
        ModelAndView modelAndView = new ModelAndView();
        Clue clue = clueService.getClueById(id);
        modelAndView.addObject(clue);
        modelAndView.setViewName("clue/convert");
        return modelAndView;
    }

    @RequestMapping("getActivityByName.do")
    @ResponseBody
    public List<Activity> getActivityByName(String aname) {
        List<Activity> activityList = activityService.getActivityByAname(aname);
        return activityList;
    }

    @RequestMapping("conversion.do")
    public ModelAndView conversion(String flag , String clueId , Tran tran , HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getName();
            boolean result = clueService.conversion(flag , clueId , tran , createBy);
            if (result){
                modelAndView.setViewName("clue/index");
            }
        return modelAndView;
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public boolean delete(String id[]) {
        return clueService.delete(id);
    }
    @RequestMapping("update.do")
    @ResponseBody
    public Map<String , Object> update(String id){
        return   clueService.getClueAndUserList(id);
    }
    @RequestMapping("updateClue.do")
    @ResponseBody
    public boolean updateClue(Clue clue , HttpServletRequest request){
        String editTime1 = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String editBy = user.getName();
        clue.setEditBy(editBy);
        clue.setEditTime(editTime1);
        int i = clueService.updateClue(clue);
        if (i > 0){
            return true;
        }
        return false;
    }
}
