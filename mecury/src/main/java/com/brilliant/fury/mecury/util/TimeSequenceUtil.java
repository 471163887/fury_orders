package com.brilliant.fury.mecury.util;

import static com.brilliant.fury.core.util.TimeUtil.DATE_MS_FORMATTER;

import java.time.LocalDateTime;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;

/**
 * @author by fury.
 * version 2020/5/24.
 */
public class TimeSequenceUtil {

    private static final int BOUND = 1000;
    private static final int THREE = 3;
    private static final String ZERO = "0";

    /**
     * generate time sequence
     *
     * @return payId.
     */
    public static String genPayId(String start) throws InterruptedException {
        String now;
        synchronized (TimeSequenceUtil.class) {
            now = DATE_MS_FORMATTER.format(LocalDateTime.now());
        }
        int nextInt = new Random().nextInt(BOUND);
        String randomNum = StringUtils.leftPad(String.valueOf(nextInt), THREE, ZERO);
        return start + now + randomNum;
    }

}
