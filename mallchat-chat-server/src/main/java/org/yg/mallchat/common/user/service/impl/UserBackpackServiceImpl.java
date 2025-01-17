package org.yg.mallchat.common.user.service.impl;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.yg.mallchat.common.common.annotation.RedissonLock;
import org.yg.mallchat.common.common.domain.enums.YesOrNoEnum;
import org.yg.mallchat.common.common.service.LockService;
import org.yg.mallchat.common.common.utils.AssertUtil;
import org.yg.mallchat.common.user.dao.UserBackpackDao;
import org.yg.mallchat.common.user.domain.entity.UserBackpack;
import org.yg.mallchat.common.user.domain.enums.IdempotentEnum;
import org.yg.mallchat.common.user.service.IUserBackpackService;

import java.util.Objects;

/**
 * @author yangang
 * @create 2025-01-16-下午8:48
 */
@Service
public class UserBackpackServiceImpl implements IUserBackpackService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private UserBackpackDao userBackpackDao;

    @Autowired
    @Lazy
    private UserBackpackServiceImpl userBackpackServiceImpl;



    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        String idempotent = getIdempotent(itemId, idempotentEnum, businessId);
        userBackpackServiceImpl.doAcquireItem(uid, itemId, idempotent);
    }

    @RedissonLock(key = "#idempotent", waitTime=5000)
    public void doAcquireItem(Long uid, Long itemId, String idempotent) {
        UserBackpack userBackpack = userBackpackDao.getByIdempotent(idempotent);
        if (Objects.nonNull(userBackpack)) {
            return;
        }
        // 发放物品
        UserBackpack insert = UserBackpack.builder()
                .uid(uid)
                .itemId(itemId)
                .status(YesOrNoEnum.NO.getStatus())
                .idempotent(idempotent)
                .build();
        userBackpackDao.save(insert);
    }

    private String getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        return String.format("%d_%d_%s", itemId, idempotentEnum.getType(), businessId);
    }
}
