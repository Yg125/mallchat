package org.yg.mallchat.common.common.event.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.yg.mallchat.common.common.event.UserBlackEvent;
import org.yg.mallchat.common.common.event.UserRegisterEvent;
import org.yg.mallchat.common.user.dao.UserDao;
import org.yg.mallchat.common.user.domain.entity.User;
import org.yg.mallchat.common.user.domain.enums.IdempotentEnum;
import org.yg.mallchat.common.user.domain.enums.ItemEnum;
import org.yg.mallchat.common.user.service.IUserBackpackService;
import org.yg.mallchat.common.user.service.cache.UserCache;
import org.yg.mallchat.common.websocket.service.WebSocketService;
import org.yg.mallchat.common.websocket.service.adapter.WebSocketAdapter;

/**
 * @author yangang
 * @create 2025-01-18-下午5:18
 */
@Component
public class UserBlackEventListener {
    @Autowired
    private UserDao userDao;
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private UserCache userCache;


    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class, phase= TransactionPhase.AFTER_COMMIT)
    public void sendMsg(UserBlackEvent event) {
        User user = event.getUser();
        webSocketService.sendMsgToAll(WebSocketAdapter.buildBlack(user));
    }

    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class, phase= TransactionPhase.AFTER_COMMIT)
    public void changeUserStatus(UserBlackEvent event) {
        userDao.invalidUid(event.getUser().getId());
    }

    @Async
    @TransactionalEventListener(classes = UserBlackEvent.class, phase= TransactionPhase.AFTER_COMMIT)
    public void evictCache(UserBlackEvent event) {
        userCache.evictBlackMap();
    }
}
