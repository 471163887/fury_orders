package com.brilliant.fury.core.base;

import java.time.LocalTime;
import javax.annotation.Resource;

/**
 * 分布式互斥任务基类
 *
 * 用法：
 * 1. 定时任务类继承BaseEnvTask
 * 2. 实现 call 方法
 * 3. 指定调度周期，任务执行环境
 *
 * @author fury.
 * version 2017/9/19.
 */
public abstract class BaseEnvTask implements CommonTask {

    @Resource
    protected EnvManager envManager;

    /**
     * 希望任务在线上环境执行.
     * 使用类名 + _hh_mm 作为任务名称
     */
    protected void executeOnProdDefaultKey() {
        String taskName = defaultTaskName();
        executeOnProd(taskName);
    }

    /**
     * 希望任务在线上环境执行.
     */
    protected void executeOnProd(String taskName) {
        if (EnvManager.PROD.equals(envManager.getEnv())) {
            execute(taskName);
        }
    }

    /**
     * 希望任务在开发环境执行.
     * 使用类名 + _hh_mm 作为任务名称
     */
    protected void executeOnDevDefaultKey() {
        String taskName = defaultTaskName();
        executeOnDev(taskName);
    }

    /**
     * 希望任务在开发测试环境执行.
     */
    protected void executeOnDev(String taskName) {
        if (EnvManager.DEV.equals(envManager.getEnv())) {
            execute(taskName);
        }
    }

    /**
     * 希望任务在测试环境执行.
     */
    protected void executeOnTest(String taskName) {
        if (EnvManager.TEST.equals(envManager.getEnv())) {
            execute(taskName);
        }
    }

    private String defaultTaskName() {
        LocalTime now = LocalTime.now();
        String taskMin = "_" + now.getHour() + "_" + now.getMinute();
        return this.getClass().getSimpleName() + taskMin;
    }

}
