package com.brilliant.fury.core.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("order_detail")
public class OrderDetail {

    @TableId(type= IdType.AUTO)
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