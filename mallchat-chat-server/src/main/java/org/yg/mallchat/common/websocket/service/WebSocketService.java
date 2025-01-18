package org.yg.mallchat.common.websocket.service;

import io.netty.channel.Channel;
import me.chanjar.weixin.common.error.WxErrorException;
import org.yg.mallchat.common.websocket.domain.vo.resp.WSBaseResp;

/**
 * @author yangang
 * @create 2025-01-13-下午10:29
 */
public interface WebSocketService {

    void connect(Channel channel);

    void handleLoginReq(Channel channel) throws WxErrorException;

    void remove(Channel channel);

    void scanLoginSuccess(Integer code, Long uid);

    void waitAuthorize(Integer code);

    void authorize(Channel channel, String token);

    void sendMsgToAll(WSBaseResp<?> msg);
}
