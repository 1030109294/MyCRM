package com.changsan.settings.service;


import com.changsan.settings.domain.User;

import javax.security.auth.login.LoginException;
import java.util.List;

public interface UserService {
     User queryUserByOne(String loginAct , String loginPwd , String addr) throws LoginException;

    List<User> queryUserAll();
}
