package org.yg.mallchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yangang
 * @create 2025-01-16-下午8:44
 */
@AllArgsConstructor
@Getter
public enum IdempotentEnum {
    UID(1, "uid"),
    MSG_ID(2, "消息id");

    private final Integer type;
    private final String desc;
}
