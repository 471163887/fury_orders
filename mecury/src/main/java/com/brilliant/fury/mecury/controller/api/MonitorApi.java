package com.brilliant.fury.mecury.controller.api;

import com.brilliant.fury.core.base.BaseController;
import com.furymonitor.monitor.sdk.api.FuryMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by fury.
 * version 2020/10/7.
 */
@Slf4j
@RestController
@RequestMapping("/api/test")
public class MonitorApi extends BaseController {

    /**
     * fury monitor 测试.
     */
    @GetMapping(value = "/aa")
    public Object test(@RequestParam("metric_name") String metricName) throws Exception {
        log.info("[metric_name]={}", metricName);
        //tagTest(metricName);
        tagMulitTest(metricName);
        return dataJson(metricName);
    }

    private void tagTest(String metricName) {

        FuryMonitor.sum(metricName, 1);
        F.uryMonitor.max(metricName + "_max", 2);
        FuryMonitor.min(metricName + "_min", 3.1);
        FuryMonitor.min(metricName + "_min", 3.2);
        FuryMonitor.avg(metricName + "_avg", 5);

        FuryMonitor.sum(metricName, 1.0, "sum_tag_a");
        FuryMonitor.sum(metricName, 1.0, "sum_tag_a");
        FuryMonitor.sum(metricName, 1.0, "sum_tag_b");

        FuryMonitor.max(metricName + "_max", 2.1, "max_tag_a");
        FuryMonitor.max(metricName + "_max", 2.2, "max_tag_b");
        FuryMonitor.max(metricName + "_max", 2.3, "max_tag_b");

        FuryMonitor.min(metricName + "_min", 3.3, "min_tag_a");
        FuryMonitor.min(metricName + "_min", 3.4, "min_tag_a");
        FuryMonitor.min(metricName + "_min", 3.5, "min_tag_b");

        FuryMonitor.avg(metricName + "_avg", 5.1, "avg_tag_a");
        FuryMonitor.avg(metricName + "_avg", 5.2, "avg_tag_b");
        FuryMonitor.avg(metricName + "_avg", 5.3, "avg_tag_b");

    }

    private void tagMulitTest(String metricName) {

        FuryMonitor.sum(metricName, 1.0, "sum_tag_a");
        FuryMonitor.sum(metricName, 1.0, "sum_tag_a,sum_tag_b");
        FuryMonitor.sum(metricName, 2, "sum_tag_b");

        FuryMonitor.max(metricName + "_max", 2.1, "max_tag_a");
        FuryMonitor.max(metricName + "_max", 2.2, "max_tag_b");
        FuryMonitor.max(metricName + "_max", 2.3, "max_tag_a,max_tag_b");

        FuryMonitor.min(metricName + "_min", 3.3, "min_tag_a,min_tag_b");
        FuryMonitor.min(metricName + "_min", 3.4, "min_tag_a");
        FuryMonitor.min(metricName + "_min", 3.5, "min_tag_b");

        FuryMonitor.avg(metricName + "_avg", 5.1, "avg_tag_a,avg_tag_b");
        FuryMonitor.avg(metricName + "_avg", 5.3, "avg_tag_b");

    }

}
