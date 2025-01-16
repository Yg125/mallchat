package org.yg.mallchat.common.common.utils;

import org.yg.mallchat.common.common.domain.dto.RequestInfo;

/**
 * @desc 请求上下文
 * @author yangang
 * @create 2025-01-16-下午3:30
 */
public class RequestHolder {
    private static final ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();
    public static void set(RequestInfo requestInfo) {
        threadLocal.set(requestInfo);
    }
    public static RequestInfo get() {
        return threadLocal.get();
    }
    public static void remove() {
        threadLocal.remove();
    }
}
