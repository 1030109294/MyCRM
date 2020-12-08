package com.changsan.workbench.service.impl;

import com.changsan.workbench.dao.ContactsActivityRelationDao;
import com.changsan.workbench.dao.ContactsDao;
import com.changsan.workbench.dao.ContactsRemarkDao;
import com.changsan.workbench.domain.Contacts;
import com.changsan.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 17925
 * @date 2020/11/21 16:03
 */
@Service("contactsService")
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    private ContactsDao contactsDao;

    @Override
    public List<Contacts> getContactByName(String fullname) {
        return contactsDao.getContactByName(fullname);
    }

    @Override
    public Contacts getContactById(String id) {
        return contactsDao.getContactById(id);
    }
}
