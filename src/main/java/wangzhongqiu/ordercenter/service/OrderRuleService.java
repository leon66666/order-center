package wangzhongqiu.ordercenter.service;

import wangzhongqiu.ordercenter.entity.OrderRule;
import wangzhongqiu.ordercenter.util.result.ResultBean;
import wangzhongqiu.ordercenter.util.result.ResultListBean;

/**
 * 订单规则引擎服务接口
 * 
 *
 */
public interface OrderRuleService {

	/**
	 * 根据业务类型与订单状态获取订单规则引擎
	 * @param tradeType
	 * @param tradeState
	 * @return ResultBean<OrderRule>
	 */
	ResultBean<OrderRule> getOrderRule(String tradeType, String tradeState);

	/**
	 * 取得需要重试的订单规则
	 * @return
	 */
	ResultListBean<OrderRule> getOrderRuleRetry();
}
