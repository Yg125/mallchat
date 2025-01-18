package org.yg.mallchat.common.user.service.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.yg.mallchat.common.user.dao.BlackDao;
import org.yg.mallchat.common.user.dao.ItemConfigDao;
import org.yg.mallchat.common.user.dao.UserRoleDao;
import org.yg.mallchat.common.user.domain.entity.ItemConfig;
import org.yg.mallchat.common.user.domain.entity.UserRole;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.yg.mallchat.common.user.domain.entity.Black;

/**
 * @author yangang
 * @create 2025-01-18-下午4:49
 */
@Component
public class UserCache {

    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private BlackDao blackDao;

    @Cacheable(cacheNames = "user", key = "'roles'+#uid")
    public Set<Long> getRoleSet(Long uid) {
        List<UserRole> userRoles = userRoleDao.listByUid(uid);
        return userRoles.stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toSet());
    }

    @Cacheable(cacheNames = "user", key = "'blackList'")
    public Map<Integer, Set<String>> getBlackMap() {
        Map<Integer, List<Black>> collect = blackDao.list().stream().collect(Collectors.groupingBy(Black::getType));
        Map<Integer, Set<String>> result = new HashMap<>();
        collect.forEach((type, list) -> {
            result.put(type, list.stream().map(Black::getTarget).collect(Collectors.toSet()));
        });
        return result;
    }

    @CacheEvict(cacheNames = "user", key = "'blackList'")
    public Map<Integer, Set<String>> evictBlackMap() {
        return null;
    }
}
