package com.changsan.workbench.web.controller;

import com.changsan.settings.domain.User;
import com.changsan.settings.service.UserService;
import com.changsan.utils.DateTimeUtil;
import com.changsan.utils.UUIDUtil;
import com.changsan.workbench.domain.*;
import com.changsan.workbench.service.ActivityService;
import com.changsan.workbench.service.ContactsService;
import com.changsan.workbench.service.CustomerService;
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
import java.util.Set;

/**
 * @author 17925
 * @date 2020/11/21 16:15
 */
@Controller
@RequestMapping("transaction")
public class TranController {
    @Autowired
    private TranService tranService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private CustomerService customerService;
    @RequestMapping("add.do")
    public ModelAndView add(){
        ModelAndView modelAndView = new ModelAndView();
        List<User> userList = userService.queryUserAll();
        modelAndView.addObject(userList);
        modelAndView.setViewName("transaction/save");
        return modelAndView;
    }
    @RequestMapping("save.do")
    public ModelAndView save(Tran tran , String customerName , HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getName();
        tran.setId(id);
        tran.setCreateTime(createTime);
        tran.setCreateBy(createBy);
        boolean result = tranService.save(tran , customerName , createBy);
        if (result){
            modelAndView.setViewName("redirect:/workbench/transaction/index.jsp");
        }
        return modelAndView;
    }
    @RequestMapping("getActivityByName.do")
    @ResponseBody
    public List<Activity> getActivityByName(String name){
        List<Activity> activityList =   activityService.getActivityByAname(name);
      return activityList;
    }
    @RequestMapping("getActivitySrc.do")
    @ResponseBody
    public Activity getActivitySrc(String id){
      return   activityService.getActivityById(id);
    }
    @RequestMapping("getContactByName.do")
    @ResponseBody
    public List<Contacts> getContactByName(String fullname){
      return contactsService.getContactByName(fullname);
    }
    @RequestMapping("getContactById.do")
    @ResponseBody
    public Contacts getContactById(String id){
        return   contactsService.getContactById(id);
    }
    @RequestMapping("getCustomerName.do")
    @ResponseBody
    public List<String> getCustomerName(String name){
        return   customerService.getCustomerName(name);
    }
    @RequestMapping("page.do")
    @ResponseBody
    public PaginationVo<Tran> page(PageVo pageVo){
        Map<String , Object> map = new HashMap<>();
        String pageSizeStr = pageVo.getPageSize();
        String pageNoStr = pageVo.getPageNo();
        int pageNo = Integer.valueOf(pageNoStr);
        int  pageSize = Integer.valueOf(pageSizeStr);
        int skipCount = (pageNo - 1) * pageSize;
        map.put("owner" , pageVo.getOwner());
        map.put("name" , pageVo.getName());
        map.put("customerName" , pageVo.getCustomerName());
        map.put("stage" , pageVo.getStage());
        map.put("contactName" , pageVo.getContactName());
        map.put("source" , pageVo.getSource());
        map.put("type" , pageVo.getType());
        map.put("skipCount" , skipCount);
        map.put("pageSize" , pageSize);
        PaginationVo<Tran> paginationVo = tranService.pageList(map);
        return paginationVo;
    }
    @RequestMapping("detail.do")
    public ModelAndView detail(String id , HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
         Tran tran = tranService.getTranById(id);
        String stage = tran.getStage();
        Map<String , String> map = (Map<String, String>) request.getServletContext().getAttribute("Pmap");
        String possibility = map.get(stage);
        tran.setPossibility(possibility);
        modelAndView.addObject("tran" , tran);
         modelAndView.setViewName("transaction/detail");
        return modelAndView;
    }

    @RequestMapping("getTranHistoryByTranId.do")
    @ResponseBody
    public List<TranHistory> getTranHistoryByTranId(String id , HttpServletRequest request){
        List<TranHistory> tranHistoryList = tranService.getTranHistoryByTranId(id);
        Map<String , String> map = (Map<String, String>) request.getServletContext().getAttribute("Pmap");
        for (TranHistory tranHistory : tranHistoryList){
            String stage = tranHistory.getStage();
            String possibility = map.get(stage);
            tranHistory.setPossibility(possibility);
        }
      return tranHistoryList;
    }
    @RequestMapping("changeStage.do")
    @ResponseBody
    public Map<String , Object> changeStage(Tran tran , HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        String editBy = user.getName();
        String editTime = DateTimeUtil.getSysTime();
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);
        Map<String , Object> map =  tranService.changeStage(tran);
       Tran t = (Tran) map.get("t");
        Map<String , String> pMap = (Map<String, String>) request.getServletContext().getAttribute("Pmap");
       String possibility = pMap.get(tran.getStage());
       t.setPossibility(possibility);
        return map;
    }
    @RequestMapping("getIcon.do")
    @ResponseBody
    public Map<String , Object> getIcon(){
        Map<String , Object> map = tranService.getIcon();
        return map;
    }
}
