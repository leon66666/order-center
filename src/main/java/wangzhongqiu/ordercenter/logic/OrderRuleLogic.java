package wangzhongqiu.ordercenter.logic;

import wangzhongqiu.ordercenter.entity.OrderRule;

import java.util.List;

/**
 * 订单规则业务处理接口
 * 
 *
 */
public interface OrderRuleLogic {

	/**
	 * 根据业务类型与订单状态获取订单规则引擎
	 * @param tradeType
	 * @param tradeState
	 * @return List<OrderRule>
	 */
	public List<OrderRule> getOrderRule(String tradeType,String tradeState);
	
	/**
	 * 根据业务类型与订单状态获取订单规则引擎
	 * @param tradeType
	 * @param tradeState
	 * @return getOne
	 */
	public OrderRule getOne(String tradeType,String tradeState,String output);

	List<OrderRule> getOrderRuleRetry();
}
