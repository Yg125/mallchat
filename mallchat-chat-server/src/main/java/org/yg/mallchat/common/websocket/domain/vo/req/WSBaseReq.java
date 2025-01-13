package org.yg.mallchat.common.websocket.domain.vo.req;


import lombok.Data;

/**
 * @author yangang
 * @create 2025-01-13-下午3:16
 */
@Data
public class WSBaseReq {
    /**
     * @see org.yg.mallchat.common.websocket.domain.enums.WSReqTypeEnum
     */
    private Integer type;
    private String data;
}
