package com.brilliant.fury.mecury.model.po;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class OrderOperate {
    private Long id;

    private String orderNo;

    private String operator;

    private Byte operationType;

    private String remark;

    private Date createAt;

}