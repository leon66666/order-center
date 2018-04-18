package wangzhongqiu.ordercenter.service;

import wangzhongqiu.ordercenter.bo.OrderBo;
import wangzhongqiu.ordercenter.entity.Order;
import wangzhongqiu.ordercenter.util.result.OrderResultBean;
import wangzhongqiu.ordercenter.util.result.ResultBasicBean;
import wangzhongqiu.ordercenter.util.result.ResultBean;
import wangzhongqiu.ordercenter.util.result.ResultListBean;

import java.util.Date;

/**
 * 订单服务接口
 * 
 *
 */
public interface OrderCenterService {

	/**
	 * 根据业务订单号 查询订单信息
	 * @param orderNo
	 * @return Order对象
	 */
	ResultBean<Order> getOrder(String orderNo);
	
	/**
	 * 
	 * @param bo: 撤销订单号，如需撤销原始订单，此字段必填写
	 * @return Order对象
	 */
	ResultBean<Order> newOrder(OrderBo bo);
	
    /**
     * 处理新订单，根据业务订单号，执行订单状态，遇到挂起或完成状态时返回结果
     * @param orderNo 业务订单号
     * @return 订单结果
     */
	OrderResultBean processOrder(String orderNo);
	
    /**
     * 处理挂起中的订单，类似处理新订单的
     * @param orderNo 业务订单号
     * @param stateInput 当前订单状态所需要的
	 * @param bussNo 流水号
     * @return 订单结果
     */
	OrderResultBean resumeOrder(String orderNo, String stateInput, String... bussNo);

    /**
     * 订单重试
     * 
     * @param orderNo
     *            业务订单号
     * @return 订单结果
     */
    OrderResultBean retryOrder(String orderNo);

    /**
     * 取消订单(把订单状态改为cancel)
     * 
     * @param orderNo
     * @return
     */
    OrderResultBean cancelOrder(String orderNo);

    /**
     * 更新订单BUSS_NO
     * @param orderNo 业务订单号
     * @param bussNo 流水号
     * @return ResultBasicBean
     */
    ResultBasicBean<String> updateOrderBussNo(String orderNo, String bussNo);


	/**
	 * 根据orderNo销毁订单
	 */

	OrderResultBean destroyOrder(String orderNo);

	/**
	 * 按条件批量查询订单
	 * @param createTimeBefore 在此时间前创建
	 * @param retryTime 重试次数
	 * @return
	 */
	ResultListBean<Order> getOrderListByTradeTypeAndStatus(Date createTimeBefore, Integer retryTime, int pageSize);

	/**
	 *
	 * @param order
	 * @return
     */
	OrderResultBean updateOrder(Order order);
}
