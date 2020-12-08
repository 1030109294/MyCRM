package com.changsan.workbench.service;

import com.changsan.workbench.domain.Customer;
import com.changsan.workbench.vo.PaginationVo;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    boolean saveCustomer(Customer customer);

    PaginationVo<Customer> pageList(Map<String, Object> map);

    boolean delete(String[] id);

    Map<String, Object> getCustomerAndUserList(String id);

    int updateCustomer(Customer customer);

    List<String> getCustomerName(String name);

    Customer getCustomerById(String customerId);
}
