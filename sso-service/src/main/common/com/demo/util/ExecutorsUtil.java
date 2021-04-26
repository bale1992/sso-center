package com.demo.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author bale
 */
public final class ExecutorsUtil {

    private static final Map<String, ExecutorService> EXECUTOR_SERVICE_MAP = new ConcurrentHashMap<>();

    private static final Map<String, ScheduledExecutorService> SCHEDULED_EXECUTOR_SERVICE_MAP = new ConcurrentHashMap<>();

    private ExecutorsUtil() {
    }

    public static ExecutorService getSingleExecutorService(String threadPoolName) {
        return EXECUTOR_SERVICE_MAP.computeIfAbsent(threadPoolName, (k) ->
                createThreadPoolExecutor(threadPoolName, 1, 1,
                        0L, TimeUnit.MILLISECONDS, 100));
    }
    
    public static ExecutorService getFixedExecutorService(int corePoolSize, String threadPoolName) {
        return EXECUTOR_SERVICE_MAP.computeIfAbsent(threadPoolName, (k) ->
                createThreadPoolExecutor(threadPoolName, corePoolSize, corePoolSize,
                        0L, TimeUnit.MILLISECONDS, 100));
    }

    public static ScheduledExecutorService getScheduledExecutorService(int corePoolSize, String threadPoolName) {
        return SCHEDULED_EXECUTOR_SERVICE_MAP.computeIfAbsent(threadPoolName, (k) ->
                new ScheduledThreadPoolExecutor(corePoolSize,
                        new ThreadFactoryBuilder().setNameFormat(threadPoolName + "_%d").build()));
    }

    private static ExecutorService createThreadPoolExecutor(String threadPoolName,
                                                            int corePoolSize,
                                                            int maximumPoolSize,
                                                            long keepAliveTime,
                                                            TimeUnit unit,
                                                            int workQueueSize) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                new LinkedBlockingDeque<>(workQueueSize),
                new ThreadFactoryBuilder().setNameFormat(threadPoolName + "_%d").build());
    }

}
