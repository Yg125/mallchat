package org.yg.mallchat.common.common.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.yg.mallchat.common.common.event.UserOnlineEvent;
import org.yg.mallchat.common.common.event.UserRegisterEvent;
import org.yg.mallchat.common.user.dao.UserDao;
import org.yg.mallchat.common.user.domain.entity.User;
import org.yg.mallchat.common.user.domain.enums.IdempotentEnum;
import org.yg.mallchat.common.user.domain.enums.ItemEnum;
import org.yg.mallchat.common.user.domain.enums.UserActiveStatusEnum;
import org.yg.mallchat.common.user.service.IUserBackpackService;
import org.yg.mallchat.common.websocket.service.IpService;

/**
 * @author yangang
 * @create 2025-01-17-下午1:34
 */
@Component
public class UserOnlineListener {
    @Autowired
    private IpService ipService;
    @Autowired
    private UserDao userDao;

    @Async
    @TransactionalEventListener(classes = UserOnlineEvent.class, phase= TransactionPhase.AFTER_COMMIT,fallbackExecution = true)
    public void saveDB(UserOnlineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setId(user.getId());
        update.setLastOptTime(user.getLastOptTime());
        update.setIpInfo(user.getIpInfo());
        update.setActiveStatus(UserActiveStatusEnum.ONLINE.getStatus());
        userDao.updateById(update);
        // 用户IP详情的解析
        ipService.refreshIpDetailAsyns(user.getId());

    }

}
