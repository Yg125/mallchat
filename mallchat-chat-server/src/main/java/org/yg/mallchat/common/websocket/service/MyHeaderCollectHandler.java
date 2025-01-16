package org.yg.mallchat.common.websocket.service;

import cn.hutool.core.net.url.UrlBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Optional;

/**
 * @description 由于websocket认证不能传cookie需要多传一遍 所以使用握手认证
 * @author yangang
 * @create 2025-01-16-下午2:03
 */
public class MyHeaderCollectHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            UrlBuilder urlBuilder = UrlBuilder.ofHttp(request.getUri()); // url uri
            Optional<String> tokenOptional=  Optional.of(urlBuilder)
                    .map(UrlBuilder::getQuery)
                    .map(k->k.get("token"))
                    .map(CharSequence::toString);
            // 如果token存在
            tokenOptional.ifPresent(s->NettyUtil.setAttr(ctx.channel(),NettyUtil.TOKEN,s));
            request.setUri(urlBuilder.getPath().toString());
        }
        ctx.fireChannelRead(msg);
    }
}
