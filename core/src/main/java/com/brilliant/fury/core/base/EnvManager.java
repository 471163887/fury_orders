package com.brilliant.fury.core.base;

import com.brilliant.fury.core.util.CollectionUtil;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author fury.
 * version 2018/5/21.
 */
@Component
public class EnvManager {

    public static final String PROD = "online";

    public static final String TEST = "test";

    public static final String DEV = "dev";

    @Resource
    private Environment environment;

    public String getEnv() {
        List<String> collect = Arrays.stream(environment.getActiveProfiles()).collect(
            Collectors.toList());
        return CollectionUtil.firstOrNull(collect);
    }

    public boolean isDev() {
        return Arrays.stream(environment.getActiveProfiles()).anyMatch(s -> s.equals(DEV));
    }

    public boolean isTest() {
        return Arrays.stream(environment.getActiveProfiles()).anyMatch(s -> s.equals(TEST));
    }

    public boolean isProd() {
        return Arrays.stream(environment.getActiveProfiles()).anyMatch(s -> s.equals(PROD));
    }

}
