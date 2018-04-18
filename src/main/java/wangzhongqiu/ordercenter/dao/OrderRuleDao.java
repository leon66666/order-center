package wangzhongqiu.ordercenter.dao;

import wangzhongqiu.ordercenter.entity.OrderRule;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单规则引擎
 * 
 *
 */
@Repository
public class OrderRuleDao {


	@Resource
	private SqlSession sqlSession;
	
	/**
	 * 根据业务类型与订单状态获取订单规则引擎
	 * @param tradeType:交易类型
	 * @param tradeState:交易状态
	 * @param output:输出结果
	 * @return List<OrderRule>
	 */
	public List<OrderRule> getOrderRule(String tradeType,String tradeState,String output){
		OrderRuleMapper mapper = sqlSession.getMapper(OrderRuleMapper.class);
		return mapper.getOrderRule(tradeType, tradeState, output);
	}
	
	/**
	 * 全量查询
	 * @return List<OrderRule>
	 */
	public List<OrderRule> getOrderRuleList(){
		OrderRuleMapper mapper = sqlSession.getMapper(OrderRuleMapper.class);
		return mapper.getOrderRuleList();
	}

	public List<OrderRule> getOrderRuleRetry() {
		OrderRuleMapper mapper = sqlSession.getMapper(OrderRuleMapper.class);
		return mapper.getOrderRuleRetry();
	}
}
