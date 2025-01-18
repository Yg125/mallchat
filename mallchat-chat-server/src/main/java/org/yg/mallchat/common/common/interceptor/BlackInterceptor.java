package org.yg.mallchat.common.common.interceptor;

import cn.hutool.extra.servlet.ServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.yg.mallchat.common.common.domain.dto.RequestInfo;
import org.yg.mallchat.common.common.exception.HttpErrorEnum;
import org.yg.mallchat.common.common.utils.RequestHolder;
import org.yg.mallchat.common.user.domain.enums.BlackTypeEnum;
import org.yg.mallchat.common.user.service.cache.UserCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * @author yangang
 * @create 2025-01-16-下午3:26
 */
@Component
public class BlackInterceptor implements HandlerInterceptor {
    @Autowired
    private UserCache userCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<Integer, Set<String>> blackMap = userCache.getBlackMap();
        RequestInfo requestInfo = RequestHolder.get();
        if(inBlackList(requestInfo.getUid(), blackMap.get(BlackTypeEnum.UID.getType()))){
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return false;
        }
        if(inBlackList(requestInfo.getIp(), blackMap.get(BlackTypeEnum.IP.getType()))){
            HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
            return false;
        }
        return true;
    }

    private boolean inBlackList(Object target, Set<String> set) {
        if(Objects.isNull(target) || CollectionUtils.isEmpty(set)) {
            return false;
        }
        return set.contains(target.toString());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RequestHolder.remove();
    }
}
