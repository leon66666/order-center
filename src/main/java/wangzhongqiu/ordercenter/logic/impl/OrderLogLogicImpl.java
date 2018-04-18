package wangzhongqiu.ordercenter.logic.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import wangzhongqiu.ordercenter.dao.OrderCenterLogDao;
import wangzhongqiu.ordercenter.entity.OrderLog;
import wangzhongqiu.ordercenter.logic.OrderLogLogic;

/**
 * 订单日志业务处理类
 */
@Service
public class OrderLogLogicImpl implements OrderLogLogic {

	private static Logger logger = Logger.getLogger(OrderLogLogicImpl.class);
    @Autowired
    private OrderCenterLogDao dao;

	public int insertOrderLog(OrderLog o) {
        logger.info("插入订单Log:" + o.toString());
		return dao.insertOrderLog(o);
	}

	@Override
	public OrderLog selectOrderLog(OrderLog orderLog) {
		return dao.selectOrderLog(orderLog);
	}

}
