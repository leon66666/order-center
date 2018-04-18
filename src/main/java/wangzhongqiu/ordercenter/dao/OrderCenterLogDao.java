package wangzhongqiu.ordercenter.dao;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import wangzhongqiu.ordercenter.entity.OrderLog;

@Repository
public class OrderCenterLogDao {
	

	@Resource
	private SqlSession sqlSession;
	
	/**
	 * 新增订单日志
	 * @param o
	 * @return
	 */
	public int insertOrderLog(OrderLog o){
		OrderLogMapper dao = sqlSession.getMapper(OrderLogMapper.class);
		return dao.insertOrderLog(o);
	}
	
	public OrderLog selectOrderLog (OrderLog o){
		OrderLogMapper dao = sqlSession.getMapper(OrderLogMapper.class);
		return dao.selectOrderLog(o);
	}
}
