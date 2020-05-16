package com.brilliant.fury.core.model.req;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author by fury.
 * version 2017/11/6.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {

    private Long id;

    private String orderNo;

    private Byte orderStatus;

    private Byte payStatus;

    private Date payTime;

    private Date sendTime;

    private Date receiveTime;

    private Date cancelledAt;

    private Date cancelledFinishAt;

    private Date createAt;

    private Date updateAt;

}