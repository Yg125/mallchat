package org.yg.mallchat.common.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yg.mallchat.common.user.domain.enums.RoleEnum;
import org.yg.mallchat.common.user.service.IRoleService;
import org.yg.mallchat.common.user.service.cache.UserCache;

import java.util.Set;

/**
 * @author yangang
 * @create 2025-01-18-下午4:48
 */
@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private UserCache userCache;

    @Override
    public boolean hasPower(Long uid, RoleEnum roleEnum) {
        Set<Long> roleSet = userCache.getRoleSet(uid);
        return isAdmin(roleSet) || roleSet.contains((roleEnum.getId()));
    }

    public boolean isAdmin(Set<Long> roleSet) {
        return roleSet.contains(RoleEnum.ADMIN.getId());
    }
}
