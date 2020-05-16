# 授权表
CREATE TABLE `biz_auth` (
  `id`         BIGINT(20)  NOT NULL AUTO_INCREMENT PRIMARY KEY
  COMMENT '自增id',
  `token`      VARCHAR(32) NOT NULL
  COMMENT '授权码',
  `workflow_engine`     TINYINT(3)  NOT NULL DEFAULT 0
  COMMENT '流程引擎抽象, 因为订单可能有不同的流程. 0:默认',
  `expire_min` INT(11)     NOT NULL DEFAULT 0
  COMMENT '过期时间, 默认不过期',
  `create_at`  TIMESTAMP   NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '创建时间',
  `update_at`  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  INDEX `idx_token`(token)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

# 订单状态信息表,会频繁修改,所有表区分大小写
CREATE TABLE `order_info` (
  `id`                  BIGINT(20)          NOT NULL AUTO_INCREMENT PRIMARY KEY
  COMMENT '自增id',
  `order_no`            VARCHAR(64)         NOT NULL
  COMMENT '订单号',
  `order_status`        TINYINT(3) UNSIGNED NOT NULL DEFAULT 0
  COMMENT '订单状态 0:待支付 1:处理中 2:支付成功 3:支付失败 4:关单 5:待发货 6:已发货 7:完成(收货确认) 8:发起退货退款 9:退款完成',
  `pay_status`          TINYINT(3) UNSIGNED NOT NULL DEFAULT 0
  COMMENT '支付状态 0:待支付 1：处理中 2:支付成功 3:支付失败 4:关单 5:待更新支付成功 6:待通知上游系统',
  `pay_time`            TIMESTAMP           NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '支付时间',
  `send_time`           TIMESTAMP           NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '发货时间',
  `receive_time`        TIMESTAMP           NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '收货时间',
  `cancelled_at`        TIMESTAMP           NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '发起退款时间',
  `cancelled_finish_at` TIMESTAMP           NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '退款完成时间',
  `create_at`           TIMESTAMP           NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '创建时间',
  `update_at`           TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  UNIQUE KEY `idx_order_no`(order_no),
  INDEX `idx_order_status`(order_status)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

# 订单详情表
CREATE TABLE `order_detail` (
  # 订单属性
  `id`               BIGINT(20)             NOT NULL AUTO_INCREMENT PRIMARY KEY
  COMMENT '自增id',
  `biz_id`           SMALLINT(6)   UNSIGNED NOT NULL
  COMMENT '创建订单的系统id',
  `order_no`         VARCHAR(64)            NOT NULL
  COMMENT '订单号',
  `buyer_id`         VARCHAR(64)            NOT NULL DEFAULT ''
  COMMENT '买家id',
  `remark`           VARCHAR(256)           NOT NULL DEFAULT ''
  COMMENT '买家备注',
  `ext_json`         VARCHAR(1024)          NOT NULL DEFAULT ''
  COMMENT '扩展字段',
  `addr_id`          BIGINT(20)             NOT NULL DEFAULT 0
  COMMENT '买家地址id',
  `need_invoice`     BOOLEAN                NOT NULL DEFAULT FALSE
  COMMENT '需要发票',
  `is_oversea`       BOOLEAN                NOT NULL DEFAULT FALSE
  COMMENT '是海外订单',
  `is_virtual`       BOOLEAN                NOT NULL DEFAULT FALSE
  COMMENT '是虚拟商品',
  # 物流相关
  `is_cod`           BOOLEAN                NOT NULL DEFAULT FALSE
  COMMENT '是货到付款',
  # 订单金额相关
  `total`            DECIMAL(19, 4)         NOT NULL
  COMMENT '订单总金额, 不能为空',
  `favourable_total` DECIMAL(19, 4)         NOT NULL DEFAULT 0.00
  COMMENT '优惠总金额',
  `logistics_pay`    DECIMAL(19, 4)         NOT NULL DEFAULT 0.00
  COMMENT '运费(物流费用)',
  `goods_total`      DECIMAL(19, 4)         NOT NULL DEFAULT 0.00
  COMMENT '货款原价合计',
  `tax_total`        DECIMAL(19, 4)         NOT NULL DEFAULT 0.00
  COMMENT '税费合计',
  `create_at`        TIMESTAMP              NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '创建时间',
  `update_at`        TIMESTAMP              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  UNIQUE KEY `idx_order_no`(order_no),
  INDEX `idx_buyer_id`(buyer_id)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

#订单支付表
CREATE TABLE `pay_detail` (
  `id`         BIGINT(20)          NOT NULL AUTO_INCREMENT PRIMARY KEY
  COMMENT '自增id',
  `order_no`   VARCHAR(64)         NOT NULL
  COMMENT '订单号',
  `pay_id`     VARCHAR(64)         NOT NULL DEFAULT ''
  COMMENT '给支付网关的支付id',
  `pay_remark` VARCHAR(64)         NOT NULL DEFAULT ''
  COMMENT '支付备注',
  `pay_total`  DECIMAL(19, 4)      NOT NULL
  COMMENT '支付金额',
  `pay_status` TINYINT(3) UNSIGNED NOT NULL DEFAULT 0
  COMMENT '支付状态 0:待支付 1：处理中 2:支付成功 3:支付失败 4:关单',
  `pay_type`   TINYINT(3) UNSIGNED NOT NULL DEFAULT 0
  COMMENT '支付方式 0:微信支付 1:支付宝',
  `pay_time`   TIMESTAMP           NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '支付时间',
  `create_at`  TIMESTAMP           NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '创建时间',
  `update_at`  TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  INDEX `idx_order_no`(order_no)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

# 买家地址表
CREATE TABLE `buyer_addr` (
  `id`              BIGINT(20)     NOT NULL AUTO_INCREMENT PRIMARY KEY
  COMMENT '自增id',
  # 买家基本信息
  `buyer_id`        VARCHAR(64)    NOT NULL DEFAULT ''
  COMMENT '买家id',
  `name`            VARCHAR(64)    NOT NULL DEFAULT ''
  COMMENT '买家姓名, 收货人姓名',
  `tel`             VARCHAR(32)    NOT NULL DEFAULT ''
  COMMENT '买家电话',
  `second_tel`      VARCHAR(32)    NOT NULL DEFAULT ''
  COMMENT '买家备用电话',
  `email`           VARCHAR(64)    NOT NULL DEFAULT ''
  COMMENT '买家电子邮件',
  # 买家地址信息
  `addr`            VARCHAR(256)   NOT NULL DEFAULT ''
  COMMENT '买家地址, 收货人地址',
  `province`        VARCHAR(32)    NOT NULL DEFAULT ''
  COMMENT '买家所在省',
  `city`            VARCHAR(32)    NOT NULL DEFAULT ''
  COMMENT '买家所在市',
  `district`        VARCHAR(64)    NOT NULL DEFAULT ''
  COMMENT '买家（收货人）所在区县',
  `post_code`       VARCHAR(16)    NOT NULL DEFAULT ''
  COMMENT '买家邮编',
  #发票信息
  `invoice_title`   VARCHAR(128)   NOT NULL DEFAULT ''
  COMMENT '发票抬头',
  `invoice_content` VARCHAR(10240) NOT NULL DEFAULT ''
  COMMENT '发票内容',
  `create_at`       TIMESTAMP      NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '创建时间',
  `update_at`       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  INDEX `idx_buyer_id`(buyer_id),
  INDEX `idx_tel`(tel)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

# 货品明细表
CREATE TABLE `good_detail` (
  `id`            BIGINT(20)     NOT NULL AUTO_INCREMENT PRIMARY KEY
  COMMENT '自增id',
  `order_no`      VARCHAR(64)    NOT NULL
  COMMENT '订单号',
  `sku_code`      VARCHAR(64)    NOT NULL DEFAULT ''
  COMMENT '货品唯一编码',
  `sn`            VARCHAR(64)    NOT NULL DEFAULT ''
  COMMENT '库存编号',
  `name`          VARCHAR(64)    NOT NULL DEFAULT ''
  COMMENT '货品名称',
  `desc`          VARCHAR(64)    NOT NULL DEFAULT ''
  COMMENT '货品描述',
  `is_gift`       BOOLEAN        NOT NULL DEFAULT FALSE
  COMMENT '是赠品',
  `quantity`      INT(11)        NOT NULL DEFAULT 0
  COMMENT '货品数量',
  `grams`         INT(11)        NOT NULL DEFAULT 0
  COMMENT '重量,单位为(克)',
  `discount`      DECIMAL(5, 2)  NOT NULL DEFAULT 0.00
  COMMENT '折扣',
  `price`         DECIMAL(19, 4) NOT NULL DEFAULT 0.00
  COMMENT '价格',
  `pre_tax_price` DECIMAL(19, 4) NOT NULL DEFAULT 0.00
  COMMENT '税前价格',
  `create_at`     TIMESTAMP      NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '创建时间',
  `update_at`     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  INDEX `idx_order_no`(`order_no`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

# 物流信息表
CREATE TABLE `logistics_info` (
  `id`               BIGINT(20)  NOT NULL AUTO_INCREMENT PRIMARY KEY
  COMMENT '自增id',
  `order_no`         VARCHAR(64) NOT NULL DEFAULT ''
  COMMENT '订单号',
  `post_id`          VARCHAR(64) NOT NULL DEFAULT ''
  COMMENT '货运单号',
  `logistics_status` TINYINT(8)  NOT NULL DEFAULT 0
  COMMENT '物流状态, 0:待揽件 1:运输 2:派送  3:签收',
  `logistics_code`   VARCHAR(32) NOT NULL DEFAULT 0
  COMMENT '物流公司代号',
  `logistics_name`   VARCHAR(32) NOT NULL DEFAULT 0
  COMMENT '物流公司名称',
  `pay_time`         TIMESTAMP   NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '支付时间',
  `send_time`        TIMESTAMP   NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '发货时间',
  `receive_time`     TIMESTAMP   NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '收货时间',
  `cancelled_at`     TIMESTAMP   NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '取消订单时间',
  `create_at`        TIMESTAMP   NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '创建时间',
  `update_at`        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  UNIQUE KEY `idx_order_no`(order_no)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

# 物流详情表
CREATE TABLE `post_info` (
  `id`        BIGINT(20)    NOT NULL AUTO_INCREMENT PRIMARY KEY
  COMMENT '自增id',
  `post_id`   VARCHAR(64)   NOT NULL DEFAULT ''
  COMMENT '货运单号',
  `post_info` VARCHAR(1024) NOT NULL DEFAULT ''
  COMMENT '货运信息',
  `create_at` TIMESTAMP     NOT NULL DEFAULT '2020-01-01 00:00:00'
  COMMENT '创建时间',
  `update_at` TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
  COMMENT '更新时间',
  INDEX `idx_post_id`(`post_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

# 订单操作记录表
CREATE TABLE `order_operate` (
  `id`             BIGINT(20)   NOT NULL AUTO_INCREMENT PRIMARY KEY
  COMMENT '自增id',
  `order_no`       VARCHAR(64)  NOT NULL
  COMMENT '订单号',
  `operator`       VARCHAR(32)  NOT NULL
  COMMENT '操作人',
  `operation_type` TINYINT(3)   NOT NULL DEFAULT 0
  COMMENT '操作类型 0: 1: 2',
  `remark`         VARCHAR(512) NOT NULL DEFAULT ''
  COMMENT '备注',
  `create_at`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '创建时间',
  INDEX `idx_order_no`(order_no)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;

# 订单标签表
CREATE TABLE `order_tag` (
  `id`       BIGINT(20)  NOT NULL AUTO_INCREMENT PRIMARY KEY
  COMMENT '自增id',
  `order_no` VARCHAR(64) NOT NULL
  COMMENT '订单号',
  `tag`      VARCHAR(32) NOT NULL
  COMMENT '标签名称',
  UNIQUE `idx_tag_order_no`(tag, order_no)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;