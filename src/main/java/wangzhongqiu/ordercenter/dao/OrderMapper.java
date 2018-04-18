package wangzhongqiu.ordercenter.dao;

import wangzhongqiu.ordercenter.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderMapper {

	/**
	 * 根据订单号获取订单
	 * @param orderNo
	 * @return
	 */
	public Order getByNo(String orderNo);
	
	/**
	 * 根据订单号修改订单状态
	 * @param orderNo
	 * @param status
	 * @return
	 */
	public int updateOrderStatus(@Param("orderNo")String orderNo,@Param("status")String status);
	
	/**
	 * 根据订单号修改订单关联ID
	 * @param orderNo
	 * @param relateOrderId
	 * @return
	 */
	public int updateOrderRelate(@Param("orderNo")String orderNo,@Param("relateOrderId")String relateOrderId);
	
	/**
	 * 新增订单
	 * @param o
	 * @return
	 */
	public int insertOrder(Order o);
	
	/**
	 * 修改订单
	 * @param o
	 * @return
	 */
	public int updateOrder(Order o);
	
	/**
	 * 修改挂起订单
	 */
	public int updateSuspOrder(Order o);

	/**
	 * 删除已备份数据
	 * @param dayOffset
	 * @return
     */
	public int deleteForBackup(@Param("dayOffset") Integer dayOffset);

	/**
	 * 按条件批量查询订单
	 *
	 * @param createTimeBefore 在此时间前创建
	 * @param retryTime        重试次数
	 * @return
	 */
	public List<Order> getOrderListByTradeTypeAndStatus(@Param("createTimeBefore") Date createTimeBefore, @Param("retryTime") Integer retryTime, @Param("pageSize") int pageSize);
}
