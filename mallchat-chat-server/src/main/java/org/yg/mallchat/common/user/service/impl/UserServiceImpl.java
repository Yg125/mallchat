package org.yg.mallchat.common.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yg.mallchat.common.user.dao.UserDao;
import org.yg.mallchat.common.user.domain.entity.User;
import org.yg.mallchat.common.user.service.UserService;

/**
 * @author yangang
 * @create 2025-01-14-下午8:40
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public Long register(User insert) {
        boolean save = userDao.save(insert);
        // todo 用户注册的事件
        return null;
    }
}
