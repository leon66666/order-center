package wangzhongqiu.ordercenter.service.impl;

import wangzhongqiu.ordercenter.dao.OrderHistoryMapper;
import wangzhongqiu.ordercenter.dao.OrderMapper;
import wangzhongqiu.ordercenter.service.OrderHistoryService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 08/06/2017.
 */
@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {

    private static Logger logger = Logger.getLogger(OrderHistoryServiceImpl.class);

    @Autowired
    private OrderHistoryMapper orderHistoryMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    public Integer backup(Integer dayOffset) {
        int count = orderHistoryMapper.insertForBackup(dayOffset);
        orderMapper.deleteForBackup(dayOffset);
        return count;
    }
}
