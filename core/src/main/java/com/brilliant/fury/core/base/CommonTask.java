
package com.brilliant.fury.core.base;

import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

/**
 * @author fury.
 * version 2017/9/8.
 */
@FunctionalInterface
public interface CommonTask<V> extends Callable<V> {

    int DEFAULT_LOCK_TIME = 59;

    default void execute(String lockKey) {
        executeLimitedTime(lockKey, DEFAULT_LOCK_TIME);
    }

    default void executeLimitedTime(String lockKey, int lockSeconds) {
        final Logger logger = LoggerFactory.getLogger(this.getClass());
        TaskCoordinater taskCoordinater = ContextHolder.getBeanByClazz(TaskCoordinater.class);
        try {
            boolean lockSuccess = taskCoordinater.isFirst(lockKey, lockSeconds);
            if (lockSuccess) {
                StopWatch stopWatch = new StopWatch();
                stopWatch.start(lockKey);
                call();
                stopWatch.stop();
                logger.info("[CommonTask_lockKey_useTime]:{},{}", lockKey, stopWatch.prettyPrint());
            } else {
                logger.info("unlock first. Task in other thread.");
            }
        } catch (Exception e) {
            logger.warn("Task key:{} execute error, msg:{}", lockKey, e.getMessage(), e);
        }
    }

}
