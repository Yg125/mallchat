package org.yg.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.yg.mallchat.common.user.dao.UserDao;
import org.yg.mallchat.common.user.domain.entity.User;
import org.yg.mallchat.common.user.service.UserService;
import org.yg.mallchat.common.user.service.WXMsgService;
import org.yg.mallchat.common.user.service.adapter.TextBuilder;
import org.yg.mallchat.common.user.service.adapter.UserAdapter;
import org.yg.mallchat.common.websocket.service.WebSocketService;

import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangang
 * @create 2025-01-14-下午3:10
 */
@Service
@Slf4j
public class WXMsgServiceImpl implements WXMsgService {
    @Autowired
    private WebSocketService webSocketService;
    /**
     *  openid和登录code的关系
     */
    private static final ConcurrentHashMap<String,Integer> WAIT_AUTHORIZE_MAP = new ConcurrentHashMap<>();

    @Value("${wx.mp.callback}")
    private String callback;

    public static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";


    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;

    @Lazy
    @Autowired
    private WxMpService wxMpService;

    @Override
    public WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage) {

        String openId = wxMpXmlMessage.getFromUser();
        Integer code = getEventKey(wxMpXmlMessage);
        if(code == null){
            return null;
        }
        User user = userDao.getByOpenId(openId);
        boolean registered = Objects.nonNull(user);                 // 是否注册
        boolean authorized = registered && StrUtil.isNotBlank(user.getAvatar());  //是否授权
        // 用户已经注册并成功授权
        if(registered && authorized){
            webSocketService.scanLoginSuccess(code, user.getId());
            //走登录成功逻辑 通过code找到channel 给channel推送消息
            return null;
        }
        // 用户未注册 就先注册
        if(!registered){
            User insert = UserAdapter.buildUserSave(openId);
            userService.register(insert);
        }
        // 推送链接让用户授权
        WAIT_AUTHORIZE_MAP.put(openId,code);
        webSocketService.waitAuthorize(code);
        String authorizeUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback + "/wx/portal/public/callBack"));
        return TextBuilder.build("请点击登录: <a href=\""+authorizeUrl+"\">登录</a>", wxMpXmlMessage);
    }

    private Integer getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        try{
            String eventKey = wxMpXmlMessage.getEventKey();
            String code = eventKey.replace("grscene_","");
            return Integer.parseInt(code);
        }catch(Exception e){
            log.error("getEventKey error eventKey:{}", wxMpXmlMessage.getEventKey());
        }
        return null;
    }

    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        String openId = userInfo.getOpenid();
        User user = userDao.getByOpenId(openId);
        // 更新用户信息
        if(StrUtil.isBlank(user.getAvatar())){
            fillUserInfo(user.getId(), userInfo);
        }
        // 通过code找到用户channel， 进行登录
        Integer code = WAIT_AUTHORIZE_MAP.remove(openId);
        webSocketService.scanLoginSuccess(code, user.getId());

    }
    private void fillUserInfo(Long uid, WxOAuth2UserInfo userInfo) {
        User user = UserAdapter.buildAuthorizeUser(uid, userInfo);
        userDao.updateById(user);
    }
}
