package com.brilliant.fury.mecury.model.enums;

import java.util.stream.Stream;

/**
 * @author by fury.
 * version 2017/11/13.
 */
public enum FavourableType {

    // 优惠类型.
    DISCOUNT(0, "商品打折"),
    COUPON(1, "使用优惠券"),
    MINUS(2, "大额满减"),
    NON(3, "无优惠");

    int code;
    String desc;

    FavourableType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * code -> FavourableType.
     */
    public static FavourableType codeOf(final int code) {
        return Stream.of(FavourableType.values()).filter(type -> type.ordinal() == code).findFirst()
            .orElse(NON);
    }

}
