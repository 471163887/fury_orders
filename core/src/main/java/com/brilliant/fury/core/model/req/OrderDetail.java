package com.brilliant.fury.core.model.req;

import java.math.BigDecimal;
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
public class OrderDetail {

    private Long id;

    private Integer bizId;

    private String orderNo;

    private String buyerId;

    private String remark;

    private String extJson;

    private Long addrId;

    private Boolean needInvoice;

    private Boolean isOversea;

    private Boolean isVirtual;

    private Boolean isCod;

    /**
     * 不能为空。
     */
    private BigDecimal total;

    private BigDecimal favourableTotal;

    private BigDecimal logisticsPay;

    private BigDecimal goodsTotal;

    private BigDecimal taxTotal;

    private Date createAt;

    private Date updateAt;

}