package org.yg.mallchat.common.websocket.domain.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangang
 * @create 2025-01-13-下午3:22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WSBaseResp<T> {
    /**
     * @see org.yg.mallchat.common.websocket.domain.enums.WSRespTypeEnum
     */
    private Integer type;
    private T data;
}
