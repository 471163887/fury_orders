package com.brilliant.fury.mecury.model.enums;

import java.util.stream.Stream;

/**
 * @author by fury.
 * version 2020/5/16.
 */
public enum EngineType {

    // 引擎类型.
    UNKNOWN(0, "未知"),
    GOTOGOOGLE(1, "go to google");

    int code;
    String desc;

    EngineType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * code -> EngineType.
     */
    public static EngineType codeOf(final int code) {
        return Stream.of(EngineType.values()).filter(type -> type.ordinal() == code).findFirst()
            .orElse(UNKNOWN);
    }

}
