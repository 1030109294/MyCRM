package com.changsan.workbench.web.controller;

import com.changsan.settings.domain.User;
import com.changsan.settings.service.UserService;
import com.changsan.utils.DateTimeUtil;
import com.changsan.utils.UUIDUtil;
import com.changsan.workbench.domain.Activity;
import com.changsan.workbench.domain.Customer;
import com.changsan.workbench.service.CustomerService;
import com.changsan.workbench.vo.PageVo;
import com.changsan.workbench.vo.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 17925
 * @date 2020/11/21 16:14
 */
@Controller
@RequestMapping("customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @RequestMapping("add.do")
    @ResponseBody
    public List<User> add(){
        return userService.queryUserAll();
    }
    @RequestMapping("save.do")
    @ResponseBody
    public boolean save(Customer customer , HttpServletRequest request){
        customer.setId(UUIDUtil.getUUID());
        customer.setCreateBy( ((User) request.getSession().getAttribute("user")).getName());
        customer.setCreateTime(DateTimeUtil.getSysTime());
        return   customerService.saveCustomer(customer);
    }
    @RequestMapping("pageList.do")
    @ResponseBody
    public PaginationVo<Customer> pageList(PageVo pageVo) {
        Map<String , Object> map = new HashMap<>();
        String pageSizeStr = pageVo.getPageSize();
        String pageNoStr = pageVo.getPageNo();
        int pageNo = Integer.valueOf(pageNoStr);
        int  pageSize = Integer.valueOf(pageSizeStr);
        int skipCount = (pageNo - 1) * pageSize;
        String name = pageVo.getName();
        map.put("name", name);
        map.put("owner", pageVo.getOwner());
        map.put("phone", pageVo.getPhone());
        map.put("website", pageVo.getWebsite());
        map.put("skipCount" , skipCount);
        map.put("pageSize", pageVo.getPageSize());
        PaginationVo<Customer> paginationVo = customerService.pageList(map);
        return paginationVo;
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public boolean delete(String id[]) {
        return customerService.delete(id);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public Map<String , Object> update(String id){
        return   customerService.getCustomerAndUserList(id);
    }
    @RequestMapping("updateCustomer.do")
    @ResponseBody
    public boolean updateCustomer(Customer customer, HttpServletRequest request){
        String editTime1 = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String editBy = user.getName();
        customer.setEditBy(editBy);
        customer.setEditTime(editTime1);
        int i = customerService.updateCustomer(customer);
        if (i > 0){
            return true;
        }
        return false;
    }
}
