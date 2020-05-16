
package com.brilliant.fury.mecury.model.po;

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
public class GoodDetail {
    private Long id;

    private String orderNo;

    private String skuCode;

    private String sn;

    private String name;

    private String desc;

    private Boolean isGift;

    private Integer quantity;

    private Integer grams;

    private BigDecimal discount;

    private BigDecimal price;

    private BigDecimal preTaxPrice;

    private Date createAt;

    private Date updateAt;
}