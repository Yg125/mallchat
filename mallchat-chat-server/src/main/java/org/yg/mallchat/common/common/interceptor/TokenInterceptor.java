package org.yg.mallchat.common.common.interceptor;

import cn.hutool.http.ContentType;
import com.google.common.base.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.yg.mallchat.common.common.domain.vo.resp.ApiResult;
import org.yg.mallchat.common.common.exception.HttpErrorEnum;
import org.yg.mallchat.common.common.utils.JsonUtils;
import org.yg.mallchat.common.websocket.service.LoginService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

/**
 * @author yangang
 * @create 2025-01-16-下午2:58
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String AUTHORIZATION_SCHEMA = "Bearer ";
    public static final String UID = "uid";

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getToken(request);
        Long validUid = loginService.getValidUid(token);
        if(Objects.nonNull(validUid)){ //用户有登录态
            request.setAttribute(UID, validUid);
        }else{ // 用户未登录
            boolean isPublicURI = isPublicURI(request);
            if(!isPublicURI){ //401
                HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
                return false;
            }
        }
        return true;
    }

    private static boolean isPublicURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        boolean isPublicURI = split.length > 3 && "public".equals(split[3]);
        return isPublicURI;
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_AUTHORIZATION);
        return Optional.ofNullable(header)
                .filter(h->h.startsWith(AUTHORIZATION_SCHEMA))
                .map(h->h.replace(AUTHORIZATION_SCHEMA,""))
                .orElse(null);
    }
}
