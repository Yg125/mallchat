package org.yg.mallchat.common.common.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yangang
 * @create 2025-01-15-下午9:25
 */
@Slf4j
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception in thread", e);
    }
}
