package org.yg.mallchat.common.common.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.yg.mallchat.common.user.domain.entity.User;

/**
 * @author yangang
 * @create 2025-01-17-下午1:58
 */
@Getter
public class UserOnlineEvent extends ApplicationEvent {
    private User user;

    public UserOnlineEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
