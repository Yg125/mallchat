package org.yg.mallchat.common.websocket.service.impl;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.yg.mallchat.common.common.constant.RedisKey;
import org.yg.mallchat.common.common.utils.JwtUtils;
import org.yg.mallchat.common.common.utils.RedisUtils;
import org.yg.mallchat.common.websocket.service.LoginService;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author yangang
 * @create 2025-01-14-下午9:45
 */
@Service
public class LoginServiceImpl implements LoginService {
    public static final int TOKEN_EXPIRE_DAYS = 3;
    public static final int TOKEN_RENEWAL_DAYS = 1;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    @Async
    public void renewalTokenIfNecessary(String token) {
        Long uid = getValidUid(token);
        String userTokenKey = getUserTokenKey(uid);
        Long expireDays = RedisUtils.getExpire(userTokenKey, TimeUnit.DAYS);
        if(expireDays == -2){ // 不存在的key
            return ;
        }
        if(expireDays < TOKEN_RENEWAL_DAYS){
            RedisUtils.expire(getUserTokenKey(uid), TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        }
    }

    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        RedisUtils.set(getUserTokenKey(uid), token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getValidUid(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if(Objects.isNull(uid)){
            return null;
        }
        String oldToken = RedisUtils.getStr(getUserTokenKey(uid));
        if(StringUtils.isBlank(oldToken)){
            return null;
        }
        return Objects.equals(oldToken, token) ? uid : null; //todo 这里字符串有问题
    }

    private String getUserTokenKey(Long uid) {
        return RedisKey.getKey(RedisKey.USER_TOKEN_STRING, uid);
    }
}
