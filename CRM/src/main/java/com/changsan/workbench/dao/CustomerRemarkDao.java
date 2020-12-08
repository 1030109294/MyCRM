package com.changsan.workbench.dao;

import com.changsan.workbench.domain.CustomerRemark;

public interface CustomerRemarkDao {

    int save(CustomerRemark customerRemark);

    int getCountByAid(String[] id);

    int deleteRemarkByAid(String[] id);
}
