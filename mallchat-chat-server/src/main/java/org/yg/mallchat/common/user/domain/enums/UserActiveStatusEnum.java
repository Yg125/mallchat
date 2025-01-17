package org.yg.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yangang
 * @create 2025-01-17-下午2:16
 */
@AllArgsConstructor
@Getter
public enum UserActiveStatusEnum {

    ONLINE(1,"在线"),
    OFFLINE(2, "离线"),
    ;
    private final Integer status;
    private final String desc;
}
