package org.yg.mallchat.common.common.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yangang
 * @create 2025-01-16-下午4:14
 */
@AllArgsConstructor
@Getter
public enum YesOrNoEnum {
    NO(0,"否"),
    YES(1,"是"),
        ;
    private final Integer status;
    private final String desc;
}
