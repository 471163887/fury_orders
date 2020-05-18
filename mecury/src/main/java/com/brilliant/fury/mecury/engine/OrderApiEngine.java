package com.brilliant.fury.mecury.engine;

import com.brilliant.fury.mecury.model.enums.EngineType;
import com.brilliant.fury.mecury.service.OrderApiService;
import com.brilliant.fury.mecury.service.impl.GoToGoogleEngineImpl;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author by fury.
 * version 2020/5/16.
 */
@Component
public class OrderApiEngine {

    @Resource
    private GoToGoogleEngineImpl goToGoogleEngine;

    private Map<EngineType, OrderApiService> orderApiMap = Maps.newHashMap();

    @PostConstruct
    private void init() {
        orderApiMap.put(EngineType.GOTOGOOGLE, goToGoogleEngine);
    }

    public OrderApiService getApiByEngine(Integer engine) {
        EngineType engineType = EngineType.codeOf(engine);
        return orderApiMap.getOrDefault(engineType, null);
    }

}
