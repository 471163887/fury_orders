package com.brilliant.fury.mecury;

import com.brilliant.fury.core.base.JsonUtil;
import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.cumulative.CumulativeDistributionSummary;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.core.instrument.distribution.HistogramSnapshot;
import io.micrometer.core.instrument.distribution.ValueAtPercentile;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

class MicroMeterTests {

    @Test
    void counterTest() {
        MeterRegistry registry = new SimpleMeterRegistry();
        Counter countera = registry.counter("database.calls", "db", "usera");
        countera.increment();
        Counter counterb = registry.counter("database.calls", "db", "userb");
        counterb.increment(2.0);
        counterb.increment(3.0);


        List<Meter> meters = registry.getMeters();
        for (Meter meter : meters) {
            Iterable<Measurement> measure = meter.measure();
            System.out.println("meter:" + meter.getId() + " measure:" + JsonUtil.toJson(measure));
        }
    }

    @Test
    void TimerTest() {
        MeterRegistry registry = new SimpleMeterRegistry();
        Timer timer = registry.timer("aegis.calls", "feature", "feature_a");
        for (int i=1; i<=100; ++i) {
            timer.record(i, TimeUnit.SECONDS);
        }
        double totalTime = timer.totalTime(TimeUnit.SECONDS);
        double max = timer.max(TimeUnit.SECONDS);
        double mean = timer.mean(TimeUnit.SECONDS);
        System.out.println("totalTime:" + totalTime + " max:" + max + " mean:" + mean);
        double percentile = timer.percentile(99, TimeUnit.SECONDS);
        System.out.println("percentile:" + percentile);
        HistogramSnapshot histogramSnapshot = timer.takeSnapshot();
        ValueAtPercentile[] valueAtPercentiles = histogramSnapshot.percentileValues();
    }


    @Test
    void distributionSummaryTest() {
        MeterRegistry registry = new SimpleMeterRegistry();
        DistributionSummary summaryPre = registry.summary("calls.summary");
        DistributionSummary summaryPre2 = registry.summary("calls.summary2");

        DistributionStatisticConfig distributionStatisticConfig = DistributionStatisticConfig.builder()
                .percentiles(0.9, 0.95, 0.999)
                .percentilePrecision(2)
                .minimumExpectedValue(1.0)
                .maximumExpectedValue(2.0)
                .bufferLength(1)
                .expiry(Duration.ofMinutes(2))
                .build();
        CumulativeDistributionSummary summary = new CumulativeDistributionSummary(summaryPre.getId(), Clock.SYSTEM, distributionStatisticConfig, 1.0, true);
        CumulativeDistributionSummary summary2 = new CumulativeDistributionSummary(summaryPre2.getId(), Clock.SYSTEM, distributionStatisticConfig, 1.0, true);
        for (int i=1; i<=100; ++i) {
            summary.record(i * 1.0);
            summary2.record(i * 2.0);
        }
        double totalAmount = summary.totalAmount();
        double max = summary.max();
        double mean = summary.mean();
        System.out.println("totalAmount:" + totalAmount + " max:" + max + " mean:" + mean);
        double percentile99 = summary.percentile(0.999);
        double percentile992 = summary2.percentile(0.999);
        System.out.println("percentile99:" + percentile99);
        System.out.println("percentile992:" + percentile992);

        double percentile95 = summary.percentile(0.95);
        double percentile952 = summary2.percentile(0.95);
        System.out.println("percentile95:" + percentile95);
        System.out.println("percentile952:" + percentile952);

        double percentile90 = summary.percentile(0.9);
        double percentile902 = summary2.percentile(0.9);
        System.out.println("percentile90:" + percentile90);
        System.out.println("percentile902:" + percentile902);
    }

    @Test
    void summaryTest() throws InterruptedException {
        DistributionStatisticConfig distributionStatisticConfig = DistributionStatisticConfig.builder()
                .percentiles(0.99)
                .percentilePrecision(1)
                .minimumExpectedValue(1.0)
                .maximumExpectedValue(1500.0)
                .bufferLength(3)
                .expiry(Duration.ofMinutes(3))
                .build();
        MeterRegistry registry = new SimpleMeterRegistry();
        DistributionSummary summaryPre = registry.summary("check.rule", "feature", "featurea");
        CumulativeDistributionSummary summary = new CumulativeDistributionSummary(summaryPre.getId(), Clock.SYSTEM, distributionStatisticConfig, 1.0, true);

        for (int i=1; i<=100; ++i) {
            summary.record(i * 1.0);
        }
        double totalAmount = summary.totalAmount();
        double max = summary.max();
        double mean = summary.mean();
        System.out.println("totalAmount:" + totalAmount + " max:" + max + " mean:" + mean);
        double percentile99 = summary.percentile(0.99);
        System.out.println("percentile99:" + percentile99);

        double percentile95 = summary.percentile(0.95);
        System.out.println("percentile95:" + percentile95);

        double percentile90 = summary.percentile(0.9);
        System.out.println("percentile90:" + percentile90);

        Thread.sleep(2 * 60 * 1000L);
    }

    @Test
    void logSummaryTest() throws InterruptedException {
        DistributionStatisticConfig distributionStatisticConfig = DistributionStatisticConfig.builder()
                .percentiles(0.99)
                .percentilePrecision(1)
                .minimumExpectedValue(1.0)
                .maximumExpectedValue(1500.0)
                .bufferLength(3)
                .expiry(Duration.ofMinutes(3))
                .build();
        MeterRegistry registry = new LoggingMeterRegistry();
        DistributionSummary summaryPre = registry.summary("check.rule", "feature", "featurea");
        CumulativeDistributionSummary summary = new CumulativeDistributionSummary(summaryPre.getId(), Clock.SYSTEM, distributionStatisticConfig, 1.0, true);

        for (int i=1; i<=100; ++i) {
            summary.record(i * 1.0);
        }
        double totalAmount = summary.totalAmount();
        double max = summary.max();
        double mean = summary.mean();
        System.out.println("totalAmount:" + totalAmount + " max:" + max + " mean:" + mean);
        double percentile99 = summary.percentile(0.99);
        System.out.println("percentile99:" + percentile99);

        double percentile95 = summary.percentile(0.95);
        System.out.println("percentile95:" + percentile95);

        double percentile90 = summary.percentile(0.9);
        System.out.println("percentile90:" + percentile90);

        Thread.sleep(2 * 60 * 1000L);
    }


}
