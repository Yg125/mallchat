package org.yg.mallchat.common.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yg.mallchat.common.common.exception.BusinessException;
import org.yg.mallchat.common.common.utils.AssertUtil;
import org.yg.mallchat.common.user.dao.UserBackpackDao;
import org.yg.mallchat.common.user.dao.UserDao;
import org.yg.mallchat.common.user.domain.entity.User;
import org.yg.mallchat.common.user.domain.entity.UserBackpack;
import org.yg.mallchat.common.user.domain.enums.ItemEnum;
import org.yg.mallchat.common.user.domain.vo.resp.UserInfoResp;
import org.yg.mallchat.common.user.service.UserService;
import org.yg.mallchat.common.user.service.adapter.UserAdapter;

import java.util.Objects;

/**
 * @author yangang
 * @create 2025-01-14-下午8:40
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserBackpackDao userBackpackDao;

    @Override
    @Transactional
    public Long register(User insert) {
        boolean save = userDao.save(insert);
        // todo 用户注册的事件
        return null;
    }

    @Override
    public UserInfoResp getUserInfo(Long uid) {
        User user = userDao.getById(uid);
        Integer modifyNameCount = userBackpackDao.getCountByValidItemId(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        return UserAdapter.buildUserInfo(user, modifyNameCount);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void modifyName(Long uid, String name) {
        User oldUser = userDao.getByName(name);
        AssertUtil.isEmpty(oldUser, "名字已经被占用了，请换一个");
        UserBackpack modifyNameItem = userBackpackDao.getFirstValidItem(uid, ItemEnum.MODIFY_NAME_CARD.getId());
        AssertUtil.isNotEmpty(modifyNameItem, "改名卡不够了，等后续活动送改名卡吧");
        // 使用改名卡
        boolean success = userBackpackDao.useItem(modifyNameItem);
        if (!success) {
            // 改名
            userDao.modifyName(uid, name);
        }

    }
}
