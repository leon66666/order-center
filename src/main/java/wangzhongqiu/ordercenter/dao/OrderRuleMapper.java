package wangzhongqiu.ordercenter.dao;

import wangzhongqiu.ordercenter.entity.OrderRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单规则引擎
 * 
 *
 */
public interface OrderRuleMapper {

	/**
	 * 根据业务类型与订单状态获取订单规则引擎
	 * @param tradeType:交易类型
	 * @param tradeState:交易状态
	 * @param output:输出结果
	 * @return List<OrderRule>
	 */
	public List<OrderRule> getOrderRule(@Param("tradeType") String tradeType,
			@Param("tradeState") String tradeState,@Param("output")String output);
	
	/**
	 * 全量查询
	 * @return List<OrderRule>
	 */
	public List<OrderRule> getOrderRuleList();

	List<OrderRule> getOrderRuleRetry();
}
