package org.yg.mallchat.common.websocket.domain.vo.resp;

import lombok.Data;

/**
 * @author yangang
 * @create 2025-01-13-下午3:22
 */
@Data
public class WSBaseResp<T> {
    /**
     * @see org.yg.mallchat.common.websocket.domain.enums.WSRespTypeEnum
     */
    private Integer type;
    private T data;
}
