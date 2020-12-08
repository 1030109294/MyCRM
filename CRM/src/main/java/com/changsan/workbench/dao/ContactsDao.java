package com.changsan.workbench.dao;

import com.changsan.workbench.domain.Contacts;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContactsDao {

    int save(Contacts contacts);

    List<Contacts> getContactByName(@Param("fullname") String fullname);

    Contacts getContactById(String id);
}
