package org.yg.mallchat.common.common.event;

import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.yg.mallchat.common.user.domain.entity.User;

/**
 * @author yangang
 * @create 2025-01-17-下午1:32
 */
@Getter
public class UserRegisterEvent extends ApplicationEvent {
    private User user;
    public UserRegisterEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}
