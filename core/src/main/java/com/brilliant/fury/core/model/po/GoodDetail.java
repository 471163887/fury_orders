
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
@TableName("good_detail")
public class GoodDetail {

    @TableId(type= IdType.AUTO)
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateAt;
}