package org.yg.mallchat.common.common.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.yg.mallchat.common.common.event.UserRegisterEvent;
import org.yg.mallchat.common.user.dao.UserDao;
import org.yg.mallchat.common.user.domain.entity.User;
import org.yg.mallchat.common.user.domain.enums.IdempotentEnum;
import org.yg.mallchat.common.user.domain.enums.ItemEnum;
import org.yg.mallchat.common.user.service.IUserBackpackService;

/**
 * @author yangang
 * @create 2025-01-17-下午1:34
 */
@Component
public class UserRegisterListener {
    @Autowired
    private IUserBackpackService userBackpackService;
    @Autowired
    private UserDao userDao;

    @Async
    @TransactionalEventListener(classes = UserRegisterEvent.class, phase= TransactionPhase.AFTER_COMMIT)
    public void sendCard(UserRegisterEvent event) {
        User user = event.getUser();
        userBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID, user.getId().toString());
    }

    @TransactionalEventListener(classes = UserRegisterEvent.class, phase= TransactionPhase.AFTER_COMMIT)
    public void sendBadge(UserRegisterEvent event) {
        // 前100名的注册徽章
        User user = event.getUser();
        int registeredCount = userDao.count();
        if(registeredCount < 10) {
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP10_BADGE.getId(), IdempotentEnum.UID, user.getId().toString());
        }else if(registeredCount < 100) {
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP100_BADGE.getId(), IdempotentEnum.UID, user.getId().toString());
        }
    }
}
