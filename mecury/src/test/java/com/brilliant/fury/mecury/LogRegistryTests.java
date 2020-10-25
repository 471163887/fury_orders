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

class LogRegistryTests {

    @Test
    void logMeterRegistryTest() throws InterruptedException {
        LoggingMeterRegistry registry = new LoggingMeterRegistry();
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
        Thread.sleep(1000L * 60 * 10);
    }

    @Test
    void breakTest() {
        for (int i = 0; i < 4; ++i){
            switch (i) {
                case 1:
                    System.out.println(i);
                    break;
                case 2:
                    System.out.println(i);
                    break;
                case 3:
                    System.out.println(i);
                    break;

            }

        }
    }


}
