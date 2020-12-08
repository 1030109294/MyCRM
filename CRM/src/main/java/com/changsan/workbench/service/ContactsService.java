package com.changsan.workbench.service;

import com.changsan.workbench.domain.Contacts;

import java.util.List;

public interface ContactsService {
    List<Contacts> getContactByName(String fullname);

    Contacts getContactById(String id);
}
