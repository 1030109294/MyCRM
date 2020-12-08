package com.changsan.workbench.service.impl;

import com.changsan.settings.dao.UserDao;
import com.changsan.settings.domain.User;
import com.changsan.workbench.dao.CustomerDao;
import com.changsan.workbench.dao.CustomerRemarkDao;
import com.changsan.workbench.domain.Activity;
import com.changsan.workbench.domain.Customer;
import com.changsan.workbench.service.CustomerService;
import com.changsan.workbench.vo.PaginationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 17925
 * @date 2020/11/21 16:08
 */
@Service("customerService")
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private CustomerRemarkDao customerRemarkDao;
    @Autowired
   private UserDao userDao;
    @Override
    public boolean saveCustomer(Customer customer) {
        boolean flag = true;
        int result = customerDao.save(customer);
        if (result != 1){
            flag = false;
        }
        return   flag;
    }

    @Override
    public PaginationVo<Customer> pageList(Map<String, Object> map) {
        PaginationVo<Customer>  paginationVo =  new PaginationVo<>();
        int total =  customerDao.totalCustomer(map);
        List<Customer> customerList =  customerDao.selectAllCustomer(map);
        paginationVo.setTotal(total);
        paginationVo.setTotalList(customerList);
        return paginationVo;
    }

    @Override
    public boolean delete(String[] id) {
        boolean flag = true;
        //查询remark中需要删除的id有多少记录
        int count1 = customerRemarkDao.getCountByAid(id);
        //删除了remark中多少条记录
        int count2 = customerRemarkDao.deleteRemarkByAid(id);
        //判断存在的记录跟删除的数量是否相等
        if (count1 != count2){
            flag = false;
        }
        //删除activity中所对应id的记录
        int count3 = customerDao.deleteCustomerByCid(id);
        //判断删除的记录于需要删除的数量是否相同
        if (count3 != id.length){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getCustomerAndUserList(String id) {
        Map<String , Object> map = new HashMap<>();
        List<User> uList =   userDao.selectAllUser();
        Customer a = customerDao.getCustomerbyId(id);
        map.put("uList" , uList);
        map.put("a" , a);
        return map;
    }

    @Override
    public int updateCustomer(Customer customer) {
       return customerDao.updateCustomer(customer);
    }

    @Override
    public List<String> getCustomerName(String name) {
        return customerDao.getCustomerName(name);
    }

    @Override
    public Customer getCustomerById(String customerId) {
        return customerDao.getCustomerbyId(customerId);
    }

}
