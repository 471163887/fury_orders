package com.brilliant.fury.mecury.controller.api;

import com.brilliant.fury.core.base.BaseController;
import com.furymonitor.monitor.sdk.api.FuryMonitor;
import com.furymonitor.monitor.sdk.model.Tags;
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
        noTagTest(metricName);
        tagTest(metricName);
        return dataJson(metricName);
    }

    private void noTagTest(String metricName) {

        FuryMonitor.sum(metricName, 1);
        FuryMonitor.max(metricName + "_max", 2);
        FuryMonitor.min(metricName + "_min", 3.1);
        FuryMonitor.min(metricName + "_min", 3.2);
        FuryMonitor.avg(metricName + "_avg", 5);

        FuryMonitor.sum(metricName, 1.0);
        FuryMonitor.sum(metricName, 1.0);
        FuryMonitor.sum(metricName, 1.0);

        FuryMonitor.max(metricName + "_max", 2.1);
        FuryMonitor.max(metricName + "_max", 2.2);
        FuryMonitor.max(metricName + "_max", 2.3);

        FuryMonitor.min(metricName + "_min", 3.3);
        FuryMonitor.min(metricName + "_min", 3.4);
        FuryMonitor.min(metricName + "_min", 3.5);

        FuryMonitor.avg(metricName + "_avg", 5.1);
        FuryMonitor.avg(metricName + "_avg", 5.2);
        FuryMonitor.avg(metricName + "_avg", 5.3);

    }

    private void tagTest(String metricName) {

        FuryMonitor.sum(metricName, 1.0);
        FuryMonitor.sum(metricName, 1.0, Tags.of("sumTagNameTest", "tag_a"));
        FuryMonitor.sum(metricName, 2, Tags.of("sumTagNameTest", "tag_b"));

        FuryMonitor.max(metricName + "_max", 2.1, Tags.of("maxTagNameTest", "max_tag_a"));
        FuryMonitor.max(metricName + "_max", 2.2, Tags.of("maxTagNameTest", "max_tag_b"));
        FuryMonitor.max(metricName + "_max", 2.3, Tags.of("maxTagNameTest", "max_tag_a", "maxTagNameTest", "max_tag_b"));

        FuryMonitor.min(metricName + "_min", 3.3, Tags.of("minTagNameTest", "min_tag_a", "minTagNameTest", "min_tag_b"));
        FuryMonitor.min(metricName + "_min", 3.4, Tags.of("minTagNameTest", "min_tag_a"));
        FuryMonitor.min(metricName + "_min", 3.5, Tags.of("minTagNameTest", "min_tag_b"));

        FuryMonitor.avg(metricName + "_avg", 5.1, Tags.of("avgTagNameTest", "avg_tag_a", "avgTagNameTest", "avg_tag_b"));
        FuryMonitor.avg(metricName + "_avg", 5.3, Tags.of("avgTagNameTest", "avg_tag_b"));

    }

}
