package com.brilliant.fury.mecury.model.enums;

import java.util.stream.Stream;

/**
 * @author by fury.
 * version 2017/11/20.
 */
public enum PayStatus {

    // 订单支付状态 4之后是异常订单的状态
    WAIT_PAY(0, "待支付"),
    PROCESSING(1, "支付中"),
    PAID(2, "支付成功"),
    CLOSE(3, "支付订单已关闭"),
    UNKNOWN(4, "未定义状态"),
    WAIT_UPDATE_PAY_SUCCESS(5, "待更新支付成功"),
    WAIT_NOTIFY(6, "待通知上游系统");

    int code;
    String desc;

    PayStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * code -> PayStatus.
     */
    public static PayStatus codeOf(final int code) {
        return Stream.of(PayStatus.values()).filter(type -> type.ordinal() == code).findFirst()
            .orElse(UNKNOWN);
    }
}
