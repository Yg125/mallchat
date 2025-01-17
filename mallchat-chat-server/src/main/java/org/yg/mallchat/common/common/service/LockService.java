package org.yg.mallchat.common.common.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yg.mallchat.common.common.exception.BusinessException;
import org.yg.mallchat.common.common.exception.CommonErrorEnum;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author yangang
 * @create 2025-01-16-下午9:32
 */
@Service
@Slf4j
public class LockService {

    @Autowired
    private RedissonClient redissonClient;

    @SneakyThrows
    public <T> T executeWithLock(String key, int waitTime, TimeUnit unit, Supplier<T> supplier){
        RLock lock = redissonClient.getLock(key);
        boolean success = lock.tryLock(waitTime, unit);
        if (!success) {
            throw new BusinessException(CommonErrorEnum.LOCK_LIMIT);
        }
        try {
            return supplier.get();//执行锁内的代码逻辑
        } finally {
            lock.unlock();
        }
    }
    public <T> T executeWithLock(String key, Supplier<T> supplier){
        return executeWithLock(key, -1, TimeUnit.MILLISECONDS, supplier);
    }
    public <T> T executeWithLock(String key, Runnable runnable){
        return executeWithLock(key, -1, TimeUnit.MILLISECONDS, ()->{
            runnable.run();
            return null;
        });
    }

    @FunctionalInterface
    public interface Supplier<T>{
        T get() throws Throwable;
    }
}