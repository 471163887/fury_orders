package com.brilliant.fury.mecury.model.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private BigDecimal total;

    private BigDecimal favourableTotal;

    private BigDecimal logisticsPay;

    private BigDecimal goodsTotal;

    private BigDecimal taxTotal;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateAt;
}