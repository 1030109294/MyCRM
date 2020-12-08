package com.changsan.settings.service.impl;

import com.changsan.settings.dao.UserDao;
import com.changsan.settings.domain.User;
import com.changsan.settings.service.UserService;
import com.changsan.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 17925
 * @date 2020/11/15 12:02
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public User queryUserByOne(String loginAct, String loginPwd , String addr) throws LoginException {
        //验证账号密码是否正确
        Map<String , String> userMap = new HashMap<>();
        userMap.put("loginAct" , loginAct);
        userMap.put("loginPwd" , loginPwd);
        User user = userDao.selectUserByOne(userMap);
        if (user == null){
            throw new LoginException("账号密码错误");
        }
        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime) < 0){
            throw new LoginException("账号已失效");
        }

        //判断锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号已锁定");
        }

        //判断ip地址
        String allowIps = user.getAllowIps();
        if (!allowIps.contains(addr)){
            throw new LoginException("该地址无妨访问此页面");
        }
        return user;
    }

    @Override
    public List<User> queryUserAll() {
        return   userDao.selectAllUser();
    }

}
