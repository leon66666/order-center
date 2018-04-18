package wangzhongqiu.ordercenter.logic;

import wangzhongqiu.ordercenter.entity.OrderLog;

/**
 * 订单日志业务处理接口
 * 
 *
 */
public interface OrderLogLogic {
	/**
	 * 新增订单日志
	 * @param o
	 * @return
	 */
	public int insertOrderLog(OrderLog o);
	
	/**
	 * 查询日志
	 */
	public OrderLog selectOrderLog(OrderLog orderLog);
}
