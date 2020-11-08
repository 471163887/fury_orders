
package com.brilliant.fury.mecury;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.cumulative.CumulativeDistributionSummary;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Slf4j
public class CostTest {

    File fileCost = new File(this.getClass().getResource("/cost.txt").getPath());
    File fileFeatureCost = new File(this.getClass().getResource("/data_20201106_060124.txt").getPath());

    List<FeaturesDTO> featuresDTOList = Lists.newArrayList();
    Map<String, CumulativeDistributionSummary> summaryMap90 = Maps.newHashMap();
    DistributionStatisticConfig config90;
    Map<String, CumulativeDistributionSummary> summaryMap95 = Maps.newHashMap();
    DistributionStatisticConfig config95;
    Map<String, CumulativeDistributionSummary> summaryMap99 = Maps.newHashMap();
    DistributionStatisticConfig config99;

    @Before
    public void setUp() throws IOException {
        List<String> strings = FileUtils.readLines(fileCost, StandardCharsets.UTF_8);
        for (String str : strings) {
            String[] newStr = str.trim().split("\\s+");
            FeaturesDTO featuresDTO = new FeaturesDTO();
            String featureName = newStr[0];
            featuresDTO.setFeatureName(featureName);
            try {
                String s = newStr[1];
                String[] split = s.trim().split("=");
                featuresDTO.setUse(Integer.parseInt(split[1]));
                featuresDTOList.add(featuresDTO);
            } catch (Exception e) {

            }
        }
        config90 =
                DistributionStatisticConfig.builder().percentiles(0.90).percentilePrecision(3).minimumExpectedValue(1.0)
                        .maximumExpectedValue(10000.0).bufferLength(3).expiry(Duration.ofMinutes(3)).build();
        config95 =
                DistributionStatisticConfig.builder().percentiles(0.95).percentilePrecision(3).minimumExpectedValue(1.0)
                        .maximumExpectedValue(10000.0).bufferLength(3).expiry(Duration.ofMinutes(3)).build();
        config99 =
                DistributionStatisticConfig.builder().percentiles(0.99).percentilePrecision(3).minimumExpectedValue(1.0)
                        .maximumExpectedValue(10000.0).bufferLength(3).expiry(Duration.ofMinutes(3)).build();

    }

    @Test
    public void testSummary() throws InterruptedException {
        long start = System.currentTimeMillis();
        FuryMeterRegistry registry = new FuryMeterRegistry();
        for (FeaturesDTO featuresDTO : featuresDTOList) {
            String featureName = featuresDTO.getFeatureName();
            String featureNameCounter = featureName + "_counter";
            Counter newCounter = registry.counter(featureNameCounter);
            newCounter.increment();

            String featureNameSummary = featureName + "_summary";
            DistributionSummary summaryPre = registry.summary(featureNameSummary);
            Meter.Id id = summaryPre.getId();
            String name = id.getName();

            /*CumulativeDistributionSummary cumulativeDistributionSummary90 = summaryMap90.get(name);
            if (null == cumulativeDistributionSummary90) {
                CumulativeDistributionSummary summary90 =
                    new CumulativeDistributionSummary(id, Clock.SYSTEM, config90, 1.0, true);
                summary90.record(featuresDTO.getUse());
                summaryMap90.put(name, summary90);
            } else {
                cumulativeDistributionSummary90.record(featuresDTO.getUse());
            }

            CumulativeDistributionSummary cumulativeDistributionSummary95 = summaryMap95.get(name);
            if (null == cumulativeDistributionSummary95) {
                CumulativeDistributionSummary summary95 =
                    new CumulativeDistributionSummary(id, Clock.SYSTEM, config95, 1.0, true);
                summary95.record(featuresDTO.getUse());
                summaryMap95.put(name, summary95);
            } else {
                cumulativeDistributionSummary95.record(featuresDTO.getUse());
            }*/

            /*CumulativeDistributionSummary summary99 =
                    new CumulativeDistributionSummary(id, Clock.SYSTEM, config99, 1.0, true);
            summary99.record(featuresDTO.getUse());
            summaryMap99.put(name, summary99);*/
            CumulativeDistributionSummary cumulativeDistributionSummary99 = summaryMap99.get(name);
            
            if (null == cumulativeDistributionSummary99) {
                CumulativeDistributionSummary summary99 =
                    new CumulativeDistributionSummary(id, Clock.SYSTEM, config99, 1.0, true);
                summary99.record(featuresDTO.getUse());
                summaryMap99.put(name, summary99);
            } else {
                cumulativeDistributionSummary99.record(featuresDTO.getUse());
            }

        }
        /*for (CumulativeDistributionSummary summary : summaryMap90.values()) {
            double percentile90 = summary.percentile(0.90);
            System.out.println("name : " + summary.getId().getName() + " tp90 : " + percentile90);
        }
        for (CumulativeDistributionSummary summary : summaryMap95.values()) {
            double percentile90 = summary.percentile(0.95);
            System.out.println("name : " + summary.getId().getName() + " tp95 : " + percentile90);
        }*/
        for (CumulativeDistributionSummary summary : summaryMap99.values()) {
            double percentile90 = summary.percentile(0.99);
            System.out.println("name : " + summary.getId().getName() + " tp99 : " + percentile90);
        }
        long use = System.currentTimeMillis() - start;
        log.info(" testSummary use={} ms", use);
        // Thread.sleep(10 * 60 * 1000L);
    }

}
