CREATE TABLE `order_` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) DEFAULT NULL COMMENT '业务订单号',
  `status` varchar(20) NOT NULL COMMENT '订单状态：初始化（目前没有用到）、处理中、挂起、完成',
  `priority` int(5) DEFAULT NULL COMMENT '服务优先级',
  `trade_type` varchar(32) DEFAULT NULL COMMENT '业务类型',
  `trade_state` varchar(50) DEFAULT NULL COMMENT '当前状态',
  `state_input` varchar(5000) DEFAULT NULL COMMENT '服务输入信息',
  `related_order_no` varchar(32) DEFAULT NULL COMMENT '关联业务流水号',
  `version` int(11) DEFAULT NULL,
  `buss_no` varchar(32) DEFAULT NULL COMMENT '流水号',
  `create_time` datetime NOT NULL COMMENT '数据库创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '数据库更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no_index` (`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16169 DEFAULT CHARSET=utf8 COMMENT='订单表';

CREATE TABLE `order_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(32) DEFAULT NULL COMMENT '业务订单号',
  `status` varchar(20) NOT NULL COMMENT '订单状态：初始化（目前没有用到）、处理中、挂起、完成',
  `priority` int(5) DEFAULT NULL COMMENT '服务优先级',
  `trade_type` varchar(32) DEFAULT NULL COMMENT '业务类型',
  `trade_state` varchar(50) DEFAULT NULL COMMENT '当前状态',
  `state_input` varchar(5000) DEFAULT NULL COMMENT '服务输入信息',
  `related_order_no` varchar(32) DEFAULT NULL COMMENT '关联业务流水号',
  `version` int(11) DEFAULT NULL,
  `buss_no` varchar(32) DEFAULT NULL COMMENT '银行流水号',
  `create_time` datetime NOT NULL COMMENT '数据库创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '数据库更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_order_history_order_no` (`order_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=684 DEFAULT CHARSET=utf8 COMMENT='订单 备份表';

CREATE TABLE `order_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_` int(11) DEFAULT NULL COMMENT '关联订单ID',
  `status` varchar(20) NOT NULL COMMENT '订单状态：初始化（目前没有用到）、处理中、挂起、完成',
  `priority` int(5) DEFAULT NULL COMMENT '服务优先级',
  `trade_type` varchar(32) DEFAULT NULL COMMENT '业务类型',
  `trade_state` varchar(50) DEFAULT NULL COMMENT '当前状态',
  `state_input` varchar(5000) DEFAULT NULL COMMENT '服务输入信息',
  `state_output` varchar(5000) DEFAULT NULL COMMENT '服务输出信息',
  `update_time` datetime DEFAULT NULL COMMENT '数据库更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=895 DEFAULT CHARSET=utf8 COMMENT='订单记录表';

CREATE TABLE `order_log_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_` int(11) DEFAULT NULL COMMENT '关联订单ID',
  `status` varchar(20) NOT NULL COMMENT '订单状态：初始化（目前没有用到）、处理中、挂起、完成',
  `priority` int(5) DEFAULT NULL COMMENT '服务优先级',
  `trade_type` varchar(32) DEFAULT NULL COMMENT '业务类型',
  `trade_state` varchar(50) DEFAULT NULL COMMENT '当前状态',
  `state_input` varchar(5000) DEFAULT NULL COMMENT '服务输入信息',
  `state_output` varchar(5000) DEFAULT NULL COMMENT '服务输出信息',
  `update_time` datetime DEFAULT NULL COMMENT '数据库更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=824 DEFAULT CHARSET=utf8 COMMENT='订单记录 备份表';

CREATE TABLE `order_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trade_type` varchar(32) DEFAULT NULL COMMENT '业务类型',
  `curr_trade_state` varchar(50) DEFAULT NULL COMMENT '当前状态',
  `curr_state_service` varchar(100) DEFAULT NULL COMMENT '调用的服务及方法',
  `curr_service_type` varchar(10) DEFAULT NULL COMMENT '服务类型',
  `curr_state_output` varchar(20) DEFAULT NULL COMMENT '当前服务结果',
  `next_status` varchar(20) DEFAULT NULL COMMENT '服务完成后订单状态',
  `next_priority` int(10) DEFAULT NULL COMMENT '下一个服务优先级',
  `next_trade_state` varchar(50) DEFAULT NULL COMMENT '下一个状态',
  `back_trade_type` varchar(32) DEFAULT NULL COMMENT '回退业务类型',
  `back_trade_state` varchar(30) DEFAULT NULL COMMENT '回退时初始状态',
  `update_time` datetime NOT NULL COMMENT '数据库更新时间',
  `is_retry` tinyint(4) DEFAULT NULL COMMENT '是否重试 1：是 0：否',
  `coefficient` int(11) DEFAULT NULL COMMENT '重试时间公式系数（执行次数的平方*系数*5）',
  `retry_time` int(11) DEFAULT NULL COMMENT '重试次数',
  `is_delay` tinyint(4) DEFAULT NULL COMMENT '是否推迟 1：推迟 2：不推迟',
  `delay_time` int(11) DEFAULT NULL COMMENT '推迟时间 单位：秒',
  PRIMARY KEY (`id`),
  KEY `tradeType_tradeState` (`trade_type`,`curr_trade_state`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='订单状态转换配置';

INSERT INTO `order_rule` VALUES ('1', 'LOAN_BID', 'INIT', 'loanBidOrderProcessor.prepareEscrowRedirection', 'LOCAL', 'SUCCESS', 'PROC', '5', 'ESCROW_SUCCESS', null, null, '2017-07-29 17:01:01', '1', null, '20', null, '30');
INSERT INTO `order_rule` VALUES ('2', 'LOAN_BID', 'INIT', 'loanBidOrderProcessor.prepareEscrowRedirection', 'LOCAL', 'FAILED', 'PROC', '5', 'CANCEL', null, null, '2017-07-29 17:01:01', '1', '1', '20', null, '30');
INSERT INTO `order_rule` VALUES ('3', 'LOAN_BID', 'ESCROW_SUCCESS', 'loanBidOrderProcessor.getUserEscrowConfirmed', 'LOCAL', 'SUCCESS', 'DONE', '5', 'DONE', null, null, '2017-07-29 17:01:01', '1', null, '20', null, '30');
INSERT INTO `order_rule` VALUES ('4', 'LOAN_BID', 'ESCROW_SUCCESS', 'loanBidOrderProcessor.getUserEscrowConfirmed', 'LOCAL', 'FAILED', 'PROC', '5', 'ESCROW_SUCCESS', null, null, '2017-07-29 17:01:01', '1', '1', '20', null, '30');
INSERT INTO `order_rule` VALUES ('5', 'LOAN_BID', 'CANCEL', 'loanBidOrderProcessor.cancelLoanBid', 'LOCAL', 'SUCCESS', 'DONE', '5', 'DONE', null, null, '2017-07-29 17:01:01', '1', null, '20', null, '30');
INSERT INTO `order_rule` VALUES ('6', 'LOAN_BID', 'CANCEL', 'loanBidOrderProcessor.cancelLoanBid', 'LOCAL', 'FAILED', 'FAILED', '5', 'FAILED', null, null, '2017-07-29 17:01:01', '1', '1', '20', null, '30');
INSERT INTO `order_rule` VALUES ('7', 'LOAN_BID', 'INIT', 'loanBidOrderProcessor.prepareEscrowRedirection', 'LOCAL', 'FATAL', 'FATAL', '5', null, null, null, '2017-07-29 17:01:01', '1', null, '20', null, '30');
SET FOREIGN_KEY_CHECKS=1;