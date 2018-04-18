package wangzhongqiu.ordercenter.service;

import wangzhongqiu.ordercenter.bo.OrderLogBo;
import wangzhongqiu.ordercenter.entity.OrderLog;
import wangzhongqiu.ordercenter.util.result.ResultBean;

/**
 * 日志服务接口
 * 
 *
 */
public interface OrderLogService {

	/**
	 * 
	 * @param orderLogBo
	 * @return OrderLog对象
	 */
	public ResultBean<OrderLog> getOrderLog(OrderLogBo orderLogBo);
}
