package wangzhongqiu.ordercenter.logic.impl;

import wangzhongqiu.ordercenter.dao.OrderRuleDao;
import wangzhongqiu.ordercenter.entity.OrderRule;
import wangzhongqiu.ordercenter.logic.OrderRuleLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderRuleLogicImpl implements OrderRuleLogic {

    private static final Logger logger = LoggerFactory.getLogger(OrderRuleLogicImpl.class);
	@Autowired
	private OrderRuleDao dao;
	
	public List<OrderRule> getOrderRule(String tradeType, String tradeState) {
        logger.info(String.format("tradeType:%s, tradeState:%s", tradeType, tradeState));
		return dao.getOrderRule(tradeType, tradeState,"");
	}

	@Override
	public OrderRule getOne(String tradeType, String tradeState,String output) {
        logger.info(String.format("tradeType:%s, tradeState:%s, output:%s", tradeType, tradeState, output));
		List<OrderRule> list = dao.getOrderRule(tradeType, tradeState,output);
		if(null != list && list.size() > 0)
			return list.get(0);
		return null;
	}

	@Override
	public List<OrderRule> getOrderRuleRetry() {
		return dao.getOrderRuleRetry();
	}
}
