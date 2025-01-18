package org.yg.mallchat.common.common.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.yg.mallchat.common.user.domain.entity.User;

/**
 * @author yangang
 * @create 2025-01-18-下午5:17
 */
@Getter
public class UserBlackEvent  extends ApplicationEvent {
    private User user;

    public UserBlackEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
