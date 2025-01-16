package org.yg.mallchat.common.common.thread;

import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Var;

import java.util.concurrent.ThreadFactory;

/**
 * @author yangang
 * @create 2025-01-15-下午9:29
 */
@AllArgsConstructor
public class MyThreadFactory implements ThreadFactory {

    private static final MyUncaughtExceptionHandler MY_UNCAUGHT_EXCEPTION_HANDLER =  new MyUncaughtExceptionHandler();
    private ThreadFactory original;

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = original.newThread(r); // 执行spring线程自己的创建逻辑
        // 额外装饰我们的线程工厂
        thread.setUncaughtExceptionHandler(MY_UNCAUGHT_EXCEPTION_HANDLER);
        return thread;
    }
}
