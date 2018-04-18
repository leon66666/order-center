package wangzhongqiu.ordercenter.service.impl;

import wangzhongqiu.ordercenter.dao.OrderLogHistoryMapper;
import wangzhongqiu.ordercenter.dao.OrderLogMapper;
import wangzhongqiu.ordercenter.service.OrderLogHistoryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志服务实现类
 * 
 *
 */

@Service
public class OrderLogHistoryServiceImpl implements OrderLogHistoryService{

	private static Logger logger = Logger.getLogger(OrderLogHistoryServiceImpl.class);

	@Autowired
	private OrderLogHistoryMapper orderLogHistoryMapper;
	@Autowired
	private OrderLogMapper orderLogMapper;

	@Override
	@Transactional
	public Integer backup(Integer dayOffset) {
		int count = orderLogHistoryMapper.insertForBackup(dayOffset);
		orderLogMapper.deleteForBackup(dayOffset);
		return count;
	}
}
