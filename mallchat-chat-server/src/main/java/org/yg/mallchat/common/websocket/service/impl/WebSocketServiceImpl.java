package org.yg.mallchat.common.websocket.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.yg.mallchat.common.common.config.ThreadPoolConfig;
import org.yg.mallchat.common.common.event.UserOnlineEvent;
import org.yg.mallchat.common.user.dao.UserDao;
import org.yg.mallchat.common.user.domain.entity.User;
import org.yg.mallchat.common.user.domain.enums.RoleEnum;
import org.yg.mallchat.common.user.service.IRoleService;
import org.yg.mallchat.common.user.service.IUserRoleService;
import org.yg.mallchat.common.websocket.domain.dto.WSChannelExtraDTO;
import org.yg.mallchat.common.websocket.domain.vo.resp.WSBaseResp;
import org.yg.mallchat.common.websocket.service.LoginService;
import org.yg.mallchat.common.websocket.service.NettyUtil;
import org.yg.mallchat.common.websocket.service.WebSocketService;
import org.yg.mallchat.common.websocket.service.adapter.WebSocketAdapter;
import java.util.Date;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangang
 * @description 专门管理websocket的逻辑，包括推拉
 * @create 2025-01-13-下午10:29
 */
@Service
public class WebSocketServiceImpl implements WebSocketService {
    @Autowired
    @Lazy
    private WxMpService wxMpService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LoginService loginService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private IRoleService roleService;
    @Autowired
    @Qualifier(ThreadPoolConfig.WS_EXECUTOR)
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 管理所有用户的连接（登录态/游客）
     */
    private static final ConcurrentHashMap<Channel, WSChannelExtraDTO> ONLINE_WS_MAP = new ConcurrentHashMap<>();

    public static final Duration DURATION = Duration.ofHours(1);
    public static final int MAXIMUM_SIZE = 10000;
    /**
     * 临时保存登录code和channel的映射关系
     */
    private static final Cache<Integer, Channel> WAIT_LOGIN_MAP = Caffeine.newBuilder()
            .maximumSize(MAXIMUM_SIZE)
            .expireAfterWrite(DURATION)
            .build();

    @Override
    public void connect(Channel channel) {
        ONLINE_WS_MAP.put(channel, new WSChannelExtraDTO());
    }


    @Override
    public void handleLoginReq(Channel channel) throws WxErrorException {
        // 生成随机code
        Integer code = generateLoginCode(channel);
        // 找微信申请代参二维码
        WxMpQrCodeTicket wxMpQrCodeTicket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(code, (int) DURATION.getSeconds());
        // 把码推送到前端
        sendMsg(channel, WebSocketAdapter.buildResp(wxMpQrCodeTicket));
    }

    @Override
    public void remove(Channel channel) {
        ONLINE_WS_MAP.remove(channel);
        // todo 用户下线
    }

    @Override
    public void scanLoginSuccess(Integer code, Long uid) {
        // 确认连接在机器上
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) {
            return;
        }
        User user = userDao.getById(uid);
        // 移除code
        WAIT_LOGIN_MAP.invalidate(code);
        // 调用登录模块 获取token
        String token = loginService.login(uid);
        // 登录成功
        loginSuccess(channel, user, token);
    }

    @Override
    public void waitAuthorize(Integer code) {
        Channel channel = WAIT_LOGIN_MAP.getIfPresent(code);
        if (Objects.isNull(channel)) {
            return;
        }
        sendMsg(channel, WebSocketAdapter.buildWaitAuthorizeResp());
    }

    @Override
    public void authorize(Channel channel, String token) {
        Long validUid = loginService.getValidUid(token);
        if (Objects.nonNull(validUid)) {
            User user = userDao.getById(validUid);
            loginSuccess(channel, user, token);
        } else {
            sendMsg(channel, WebSocketAdapter.buildInvalidTokenResp());
        }
    }

    @Override
    public void sendMsgToAll(WSBaseResp<?> msg) {
        ONLINE_WS_MAP.forEach((channel, ext)->{
            threadPoolTaskExecutor.execute(()->sendMsg(channel, msg));
        });
    }

    private void loginSuccess(Channel channel, User user, String token) {
        // 保存channel对应的uid 因为刷新之后channel会断开重新分配
        WSChannelExtraDTO wsChannelExtraDTO = ONLINE_WS_MAP.get(channel);
        wsChannelExtraDTO.setUid(user.getId());
        // 推送成功消息
        sendMsg(channel, WebSocketAdapter.buildResp(user, token, roleService.hasPower(user.getId(), RoleEnum.CHAT_MANAGER)));
        // 用户上线成功的事件
        user.setLastOptTime(new Date());
        user.refreshIp(NettyUtil.getAttr(channel, NettyUtil.IP));
        applicationEventPublisher.publishEvent(new UserOnlineEvent(channel, user));
    }

    public void sendMsg(Channel channel, WSBaseResp<?> resp) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(resp)));
    }

    private Integer generateLoginCode(Channel channel) {
        Integer code;
        do {
            code = RandomUtil.randomInt(Integer.MAX_VALUE);
        } while (Objects.nonNull(WAIT_LOGIN_MAP.asMap().putIfAbsent(code, channel)));
        return code;
    }
}
