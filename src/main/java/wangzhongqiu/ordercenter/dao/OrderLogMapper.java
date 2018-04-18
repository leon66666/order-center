package wangzhongqiu.ordercenter.dao;

import wangzhongqiu.ordercenter.entity.OrderLog;
import org.apache.ibatis.annotations.Param;

public interface OrderLogMapper {
	
	/**
	 * 新增订单日志
	 * @param o
	 * @return
	 */
	public int insertOrderLog(OrderLog o);
	
	public OrderLog selectOrderLog (OrderLog o);

	public void deleteForBackup(@Param("dayOffset") Integer dayOffset);
}
