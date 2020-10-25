package com.brilliant.fury.core.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("biz_auth")
public class BizAuth {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String token;

    private Integer workflowEngine;

    private Integer expireMin;

    private Date createAt;

    private Date updateAt;

}