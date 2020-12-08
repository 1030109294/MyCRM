package com.changsan.settings.dao;

import com.changsan.settings.domain.User;


import java.util.List;
import java.util.Map;


public interface UserDao {
    User selectUserByOne(Map<String , String> map);

    List<User> selectAllUser();

}
