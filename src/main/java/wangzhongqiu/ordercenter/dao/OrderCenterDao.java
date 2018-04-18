package wangzhongqiu.ordercenter.dao;

import wangzhongqiu.ordercenter.entity.Order;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Repository
public class OrderCenterDao {

	@Resource
	private SqlSession sqlSession;

	public Order getByNo(String orderNo) {
		OrderMapper dao = sqlSession.getMapper(OrderMapper.class);
		return dao.getByNo(orderNo);
	}

	public int updateOrderStatus(String orderNo, String status) {
		OrderMapper dao = sqlSession.getMapper(OrderMapper.class);
		return dao.updateOrderStatus(orderNo, status);
	}

	public int updateOrderRelate(String orderNo, String relateOrderId) {
		OrderMapper dao = sqlSession.getMapper(OrderMapper.class);
		return dao.updateOrderRelate(orderNo, relateOrderId);
	}

	public int insertOrder(Order o) {
		OrderMapper dao = sqlSession.getMapper(OrderMapper.class);
		return dao.insertOrder(o);
	}

	public int updateOrder(Order o) {
		OrderMapper dao = sqlSession.getMapper(OrderMapper.class);
		return dao.updateOrder(o);
	}

	public int updateSuspOrder(Order o) {
		OrderMapper dao = sqlSession.getMapper(OrderMapper.class);
		return dao.updateSuspOrder(o);
	}

	/**
	 * 按条件批量查询订单
	 *
	 * @param createTimeBefore 在此时间前创建
	 * @param retryTime        重试次数
	 * @param pageSize			分页数
	 * @return
	 */
	public List<Order> getOrderListByTradeTypeAndStatus(Date createTimeBefore, Integer retryTime, int pageSize) {
		OrderMapper mapper = sqlSession.getMapper(OrderMapper.class);
		return mapper.getOrderListByTradeTypeAndStatus(createTimeBefore, retryTime, pageSize);
	}
}