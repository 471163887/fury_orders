
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
public class BuyerAddr {
    private Long id;

    private String buyerId;

    private String name;

    private String tel;

    private String secondTel;

    private String email;

    private String addr;

    private String province;

    private String city;

    private String district;

    private String postCode;

    private String invoiceTitle;

    private String invoiceContent;

    private Date createAt;

    private Date updateAt;

}