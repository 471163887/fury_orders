package com.brilliant.fury.mecury.engine;

import com.brilliant.fury.mecury.model.enums.EngineType;
import com.brilliant.fury.mecury.service.OrderApiService;
import com.brilliant.fury.mecury.service.impl.GoToGoogleEngineImpl;
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

    public OrderApiService getApiByEngine(int engine) {
        EngineType engineType = EngineType.codeOf(engine);
        switch (engineType) {
            case GOTOGOOGLE:
                return goToGoogleEngine;
            default:
                return null;
        }
    }

}
