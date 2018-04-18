package wangzhongqiu.ordercenter.logic.impl;

import com.alibaba.fastjson.JSON;
import wangzhongqiu.ordercenter.dao.OrderCenterDao;
import wangzhongqiu.ordercenter.entity.Order;
import wangzhongqiu.ordercenter.logic.OrderLogic;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 订单业务处理类
 */
@Service
public class OrderLogicImpl implements OrderLogic {
    private static Logger logger = Logger.getLogger(OrderLogicImpl.class);
    @Autowired
    private OrderCenterDao dao;

    /**
     * 根据业务订单号 查询订单信息
     *
     * @param orderNo
     * @return Order对象
     */
    public Order getByNo(String orderNo) {
        return dao.getByNo(orderNo);
    }

    public int updateOrderStatus(String orderNo, String status) {
        logger.info(String.format("更新订单信息，orderNo:%s, status:%s", orderNo, status));
        return dao.updateOrderStatus(orderNo, status);
    }

    public int insertOrder(Order o) {
        logger.info(String.format("插入订单信息，order:%s", JSON.toJSONString(o)));
        return dao.insertOrder(o);
    }

    @Override
    public int updateOrderRelate(String orderNo, String relateOrderId) {
        return dao.updateOrderRelate(orderNo, relateOrderId);
    }

    @Override
    public int updateOrder(Order o) {
        logger.info(String.format("更新订单信息，order:%s", JSON.toJSONString(o)));
        return dao.updateOrder(o);
    }

    @Override
    public int updateSuspOrder(Order o) {
        logger.info(String.format("更新订单信息，order:%s", JSON.toJSONString(o)));
        return dao.updateSuspOrder(o);
    }

    @Override
    public List<Order> getOrderListByTradeTypeAndStatus(Date createTimeBefore, Integer retryTime, int pageSize) {
        return dao.getOrderListByTradeTypeAndStatus(createTimeBefore, retryTime, pageSize);
    }
}
