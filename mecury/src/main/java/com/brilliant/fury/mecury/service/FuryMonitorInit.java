package com.brilliant.fury.mecury.service;

import com.furymonitor.monitor.sdk.core.MonitorInit;
import com.furymonitor.monitor.sdk.task.TransferClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author : fury
 * @date : 2020/10/25
 */

@Service
public class FuryMonitorInit implements ApplicationContextAware {

    @Value("${fury.monitor.service.host}")
    public String serviceHost = "127.0.0.1";
    @Value("${fury.monitor.service.port}")
    public int servicePort = 9998;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        new Thread(() -> {
            TransferClient transferClient = TransferClient.getInstance();
            transferClient.connect(serviceHost, servicePort);
        }).start();
        MonitorInit.init("test", "aa", "test");
    }
}
