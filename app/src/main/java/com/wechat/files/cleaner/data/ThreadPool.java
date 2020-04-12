package com.wechat.files.cleaner.data;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * author:davidinchina on 2019/5/8 16:04
 * email:davicdinchina@gmail.com
 * version:1.0.0
 * des:
 */
public class ThreadPool {
    private static volatile ThreadPool instance;
    public ThreadPoolExecutor threadPoolExecutor = null;

    private ThreadPool() {
    }
    public static ThreadPool getInstance() {
        if (instance == null) {
            synchronized (ThreadPool.class) {
                if (instance == null) {
                    instance = new ThreadPool();
                    int corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
                    int maxNumPoolSize = corePoolSize + 1;
                    long keepAliveTime = 10;
                    TimeUnit unit = TimeUnit.SECONDS;
                    instance.threadPoolExecutor =  new ThreadPoolExecutor(corePoolSize,
                            maxNumPoolSize,
                            keepAliveTime,
                            unit,
                            new LinkedBlockingQueue<>(),
                            Executors.defaultThreadFactory(),
                            new ThreadPoolExecutor.AbortPolicy());
                }
            }
        }
        return instance;
    }
}
