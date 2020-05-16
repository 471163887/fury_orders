package com.brilliant.fury.mecury.service.impl;

import com.brilliant.fury.mecury.util.SnowflakeIdWorker;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author by fury.
 * version 2020/5/16.
 */
@Slf4j
@Service
public class OrderNoPoolImpl {

    private ConcurrentLinkedQueue<String> orderNoQueue = Queues.newConcurrentLinkedQueue();

    @Value("${order.pool.min.size}")
    private Integer minSize;
    @Value("${order.pool.new.count}")
    private Integer newCount;
    @Value("${order.pool.work.id}")
    private Integer workId;
    @Value("${order.pool.datacenter.id}")
    private Integer dataCenterId;

    private SnowflakeIdWorker idWorker;

    @PostConstruct
    private void init() {
        log.info("[FURY_ORDERS_MECURY_OrderNoPool_init_params]minSize={},newCount={},workId={}," +
            "datacenterId={}", minSize, newCount, workId, dataCenterId);
        idWorker = new SnowflakeIdWorker(workId, dataCenterId);
    }

    public String getOrderNo() {
        String orderNo;
        try {
            orderNo = orderNoQueue.remove();
            if (orderNoQueue.size() < minSize) {
                adjustQueue();
            }
        } catch (NoSuchElementException e) {
            adjustQueue();
            orderNo = orderNoQueue.remove();
        }
        log.info("[FURY_ORDERS_MECURY_getOrderNo]=:{}", orderNo);
        return orderNo;
    }

    public Integer getLiveCount() {
        return orderNoQueue.size();
    }

    private void adjustQueue() {
        List<String> newOrderNos = genOrderNos();
        orderNoQueue.addAll(newOrderNos);
    }

    private List<String> genOrderNos() {
        List<String> orders = Lists.newArrayList();
        for (int i = 0; i < newCount; i++) {
            long id = idWorker.nextId();
            orders.add(String.valueOf(id));
        }
        return orders;
    }
}
