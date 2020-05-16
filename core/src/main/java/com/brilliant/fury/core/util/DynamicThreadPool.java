package com.brilliant.fury.core.util;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程池的线程数如何估算：
 *
 * 看实际运行时线程池的 activeCount。
 *
 * @author by fury.
 * version 2018/10/26.
 */
public class DynamicThreadPool {

    private static final Logger log = LoggerFactory.getLogger(DynamicThreadPool.class);

    public static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    public static final int MAX_POOL_SIZE = CORE_POOL_SIZE * 11;

    public static final int QUEUE_SIZE = CORE_POOL_SIZE * 100;

    public static final int QUEUE_BLOCK_PERCENT = 10;

    public static final long LOG_LIMIT_TIME = 200L;

    public static final int DYNAMIC_MAX_POOL_SIZE = 1023;

    public static Map<String, ThreadPoolExecutor> EXECUTOR_MAP = Maps.newHashMap();

    /**
     * 初始化线程池.
     */
    public static ThreadPoolExecutor initExecutor(int corePoolSize, final int maximumPoolSize,
        int queueSize, final String monitorKey) {

        BlockingQueue<Runnable> workQueue;
        if (queueSize == 0) {
            workQueue = new SynchronousQueue<>();
        } else {
            workQueue = new ArrayBlockingQueue<>(queueSize);
        }
        ThreadFactory threadFactory = genThreadFactory(monitorKey);
        ThreadPoolExecutor.DiscardOldestPolicy policy = genDiscardOldestPolicy(monitorKey);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
            maximumPoolSize, 0L, TimeUnit.MILLISECONDS,
            workQueue, threadFactory, policy) {

            private ThreadLocal<Long> taskExecuteStart = new ThreadLocal<>();

            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                super.beforeExecute(t, r);
                //大致的活跃线程数
                int activeCount = this.getActiveCount();

                int blockTaskCount = this.getQueue().size();
                taskExecuteStart.set(System.currentTimeMillis());

                //排队任务超过最大线程数的10% 认为队列拥堵
                if (blockTaskCount > maximumPoolSize / QUEUE_BLOCK_PERCENT) {
                    //线程队列任务堆积
                    log.warn("[ARCH_ZZJODIS_Before_Slow]Executor:{}", monitorKey);
                }

            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                Long start = taskExecuteStart.get();
                Long executeTime = System.currentTimeMillis() - start;
                if (executeTime > LOG_LIMIT_TIME) {
                    log.warn("[ARCH_ZZJODIS]Task name: {} execute ms: {}", monitorKey, executeTime);
                }
                taskExecuteStart.remove();
                super.afterExecute(r, t);
            }

            @Override
            protected void terminated() {
                log.info(monitorKey + "_Terminated");
            }
        };
        EXECUTOR_MAP.put(monitorKey, threadPoolExecutor);
        return threadPoolExecutor;
    }

    /**
     * 在拒绝策略触发时打印异常日志.并尝试增加线程池最大线程数
     */
    private static ThreadPoolExecutor.DiscardOldestPolicy genDiscardOldestPolicy(
        final String monitorKey) {
        return new ThreadPoolExecutor.DiscardOldestPolicy() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                super.rejectedExecution(r, executor);

                int maximumPoolSize = executor.getMaximumPoolSize();
                if (maximumPoolSize < DYNAMIC_MAX_POOL_SIZE) {
                    modMaxPoolSize(monitorKey, DYNAMIC_MAX_POOL_SIZE);
                }
                log.error("[ARCH_ZZJODIS_Rejected]{} maxPoolSize:{}", maximumPoolSize);
            }
        };
    }

    /**
     * 执行的任务抛出异常时打印日志.
     */
    private static ThreadFactory genThreadFactory(String monitorKey) {
        return runnable -> {
            Thread thread = Executors.defaultThreadFactory().newThread(runnable);
            thread.setUncaughtExceptionHandler((t, e) -> {
                log.error("[ARCH_ZZJODIS_Uncaught_Exception]msg:{}", e.getMessage(), e);

            });
            thread.setName(monitorKey + "-" + thread.getName());
            return thread;
        };
    }

    /**
     * 调整核心池参数
     *
     * @return 旧的核心池数。
     */
    private static int modCoreSize(String monitorKey, int newCoreSize) {
        ThreadPoolExecutor threadPoolExecutor = EXECUTOR_MAP.get(monitorKey);
        if (null == threadPoolExecutor) {
            return 0;
        }
        int corePoolSize = threadPoolExecutor.getCorePoolSize();
        threadPoolExecutor.setCorePoolSize(newCoreSize);
        return corePoolSize;
    }

    /**
     * 调整最大线程参数
     *
     * @return 旧的最大线程数。
     */
    private static int modMaxPoolSize(String monitorKey, int newMaxPoolSize) {
        ThreadPoolExecutor threadPoolExecutor = EXECUTOR_MAP.get(monitorKey);
        if (null == threadPoolExecutor) {
            return 0;
        }
        int maximumPoolSize = threadPoolExecutor.getMaximumPoolSize();
        threadPoolExecutor.setMaximumPoolSize(newMaxPoolSize);
        return maximumPoolSize;
    }

}
