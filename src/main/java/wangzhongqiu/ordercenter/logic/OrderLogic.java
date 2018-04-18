package wangzhongqiu.ordercenter.logic;

import wangzhongqiu.ordercenter.entity.Order;

import java.util.Date;
import java.util.List;

/**
 * 订单业务处理接口
 * 
 *
 */
public interface OrderLogic {

	/**
	 * 根据业务订单号 查询订单信息
	 * @param orderNo
	 * @return Order对象
	 */
	public Order getByNo(String orderNo);
	
	/**
	 * 根据订单号修改订单状态
	 * @param orderNo
	 * @param status
	 * @return
	 */
	public int updateOrderStatus(String orderNo,String status);
	
	/**
	 * 根据订单号修改订单关联Id
	 * @param orderNo
	 * @param relateOrderId
	 * @return
	 */
	public int updateOrderRelate(String orderNo,String relateOrderId);
	
	/**
	 * 新增订单
	 * @param o
	 * @return
	 */
	public int insertOrder(Order o);
	
	/**
	 * 更新订单
 	 * @param o
	 */
	public int updateOrder(Order o);
	
	/**
	 * 更新挂起订单
	 */
	public int updateSuspOrder(Order o);

	/**
	 * 按条件批量查询订单
	 * @param createTimeBefore 在此时间前创建
	 * @param retryTime 重试次数
	 * @return
	 */
	List<Order> getOrderListByTradeTypeAndStatus(Date createTimeBefore, Integer retryTime, int pageSize);

}
