package com.changsan.workbench.dao;

import com.changsan.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer1);

    int totalCustomer(Map<String, Object> map);

    List<Customer> selectAllCustomer(Map<String, Object> map);

    int deleteCustomerByCid(String[] id);

    Customer getCustomerbyId(String id);

    int updateCustomer(Customer customer);

    List<String> getCustomerName(String name);

    List<Customer> getCustomerByNameList(String customerName);
}
