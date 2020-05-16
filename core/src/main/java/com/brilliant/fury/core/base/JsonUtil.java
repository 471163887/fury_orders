package com.brilliant.fury.core.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author by fury.
 * version 2018/4/9.
 */
public class JsonUtil {

    public static final <T> String toJson(T t) {
        return JSON.toJSONString(t, SerializerFeature.DisableCircularReferenceDetect);
    }

}
